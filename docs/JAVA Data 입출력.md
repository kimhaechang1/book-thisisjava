## 데이터 스트림

프로그램은 다른 프로그램 혹은 파일로 부터 데이터를 입력받거나 출력하여 전송할 수 있다

JAVA 에서는 프로그램을 기준으로 들어오는 데이터를 입력 스트림 `InputStream`

프로그램을 기준으로 나가는 데이터를 출력 스트림 `OutputStream` 이라고 한다.

문자 데이터를 주고받을 때에는 `OOOReader` 와 `OOOWriter`

바이트 데이터를 주고받을 때에는 `OOOInputStream` 과 `OOOOutputStream`이 사용된다.

또한 JAVA의 객체 데이터도 주고받을 수 있는데, 전송할 때 전송자는 객체를 직렬화(Serialize) 라고 하고

수신자는 전송 받아서는 해당 객체를 역직렬화(Deserialize)를 통해 내부 필드 데이터를 접근한다.

데이터를 전송할 때에는 주로 버퍼를 사용하는데, 이는 효과적으로 데이터 전송과 수신의 시간을 줄여준다.

예를들어서 1024 바이트의 데이터를 1바이트씩 전송할 때와 1024바이트를 128 바이트씩 전송할때를 생각해보면

둘의 반복횟수가 많이 차이난다. 이러한 버퍼 등의 편의성을 위해서 `BufferedReader`와 `InputStreamReader`등의 보조스트림을 혼합하여 사용한다.

### 바이트 입출력시 조심해야 하는 부분

바이트데이터를 입력 받거나 출력할 때에는 `InputStream`을 사용한다.

여기서 한가지 재밌는점은, 입출력 할 때 사용사하는 오버로딩 된 메소드 중 `read(int b)`와 `write(int b)`가 있다.

매개변수의 타입이 `int` 라고해서 1바이트보다 큰 데이터 즉 `byte` 자료형 범위를 넘어서는 데이터를 전송하려 하면

비트가 짤려서 나온다.

GPT 말로는 내부적으로 `int`를 받아서 하위 8개 비트 즉 1바이트만을 전송한다고 한다.

```java
public class Practice {
    public static void main(String[] args) throws Exception{
        File file = new File("src/DataIO/bytedata/test.db");
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream os = new FileOutputStream(file);
        InputStream is = new FileInputStream(file);
        try(os;is){
            int data = 300;
            // 00000000 00000000 00000001 00101100
            os.write(data);
            int r;
            while((r = is.read())!= -1){
                System.out.println(r); // 44가 튀어나옴
                // 44 -> 00000000 00000000 00000000 00101100
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
```

이런 경우를 조심하기 위해서라도 보조 스트림을 사용하는것이 좋다.

그래서 위의 경우를 보조 스트림 `DataInputStream` 과 `DataOutputStream`을 사용해보면 자료형에 맞게 데이터를 쓰고 읽을 수 있다.

우선 1바이트씩 읽어들인다는걸 확인하기 위해 `DataOutputStream`을 사용해서 300을 저장하고

`InputStream`을 통해 읽어들여보자.

```java
public class Practice {
    public static void main(String[] args) throws Exception{
        File file = new File("src/DataIO/bytedata/test.db");
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream os = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(is);
        try(os;is;dos;dis){
            int data = 300;
            dos.writeInt(data);
            int r;
            while((r = is.read())!= -1){
                System.out.println(r);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
```

```
0
0
1
44
```

위와 같은 출력이 나오는 이유는 1바이트씩 읽어들였기 때문이다.

위의 상황에서 300의 값을 2진수로 나타내보면

```
00000000 00000000 00000001 00101100
```

으로 쓰였을 것이다.

여기서 읽을때 보조스트림을 사용하지 않았으므로, 1바이트 씩 읽어들이고 이를 `int` 형으로 형변환 하여 출력하기에

```
00000000 -> 0
00000000 -> 0
00000001 -> 1
00101100 -> 44
```

가 된다.

이제 다시 읽어들일 때도 보조 스트림을 사용해보면

```java
public class Practice {
    public static void main(String[] args) throws Exception{
        File file = new File("src/DataIO/bytedata/test.db");
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream os = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(is);
        try(os;is;dos;dis){
            int data = 300;
            dos.writeInt(data);
            int r;
            while((r = dis.readInt())!= -1){
                System.out.println(r);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
```

```
300
java.io.EOFException
	at java.base/java.io.DataInputStream.readFully(DataInputStream.java:210)
	at java.base/java.io.DataInputStream.readInt(DataInputStream.java:385)
	at DataIO.bytedata.Practice.main(Practice.java:20)
```

이상하게 `EOFException` 이 발생한다.

그 이유는 readInt() 메소드는 내부적으로 InputStream의 read() 메소드를 사용하고,

read() 메소드에 인자로 8바이트짜리 byte배열을 버퍼로 사용하고 4바이트 만큼 읽도록 명령한다.

그런데 이 과정속에서 4바이트씩 읽어오고, 그 다음 4바이트를 읽으려 할 때, EOF가 발생하여 read() 메소드는 -1을 반환하게되고

여기서 -1을 반환받으면 EOFException을 던지도록 구현되어있다.

따라서 `available` 메소드를 사용해서 읽어들인 데이터의 길이가 `int` 자료형 크기보다 작은지 검사하거나

`EOFException`이 발생했을 때 읽기를 종료하도록 유도해야 한다.

```java
public class Practice {
    public static void main(String[] args) throws Exception{
        File file = new File("src/DataIO/bytedata/test.db");
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream os = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(is);
        try(os;is;dos;dis){ // try-with resources
            int data = 300;
            dos.writeInt(data);
            int r;
            while(dis.available() > 0){
                // 읽은 데이터가 0보다 클때만 동작하도록
                System.out.println(dis.readInt());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
```

### 버퍼에 대한 실행속도 시험

입출력에서 일반적으로 한 바이트씩 쓴다고 생각하면 굉장히 오래걸리는 작업임으로

내부적인 버퍼를 사용해서 모아서 쓴다.

그 과정에서 버퍼를 쉽게 사용가능 하도록 도와주는 보조스트림으로 `BufferedReader`혹은 `BufferedWriter` 등과 같은 것들이 존재한다.

이들은 모두 내부적으로 `char` 버퍼가 존재하고

생성자를 통해 따로 설정해주지 않는다면

기본 버퍼 사이즈는 8192가 된다.

```java
private static final int DEFAULT_CHAR_BUFFER_SIZE = 8192;
public BufferedReader(Reader in, int sz) {
    super(in);
    if (sz <= 0)
        throw new IllegalArgumentException("Buffer size <= 0");
    this.in = in;
    cb = new char[sz]; // 내부적으로 읽을때 사용하는 char 버퍼
    nextChar = nChars = 0;
}
public BufferedReader(Reader in) {
    this(in, DEFAULT_CHAR_BUFFER_SIZE);
}
```

10000줄의 문자열을 각각

그러면 `Reader`의 `read()`를 사용했을 때와

`Reader`의 `read(char [] cbuf)`를 사용했을 때와

`BufferedReader`의 `readLine()` 을 사용했을 때의 속도차이가 얼마나 발생하는지 확인해보았다.

여기서 `char [] cbuf`의 크기는 `BufferedReader`의 내부적인 기본 버퍼 사이즈와 동일하게 주었다.

```java
public class Practice {
    public static void main(String[] args) throws IOException {
        File file = new File("src/DataIO/chardata/data.txt");
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        long startTime;
        long endTime;
        int r;
        char [] cbuf = new char[8192];
        String line;
        try (writer;bw;reader;br) {
            for(int i = 0;i<10000;i++){
                String str = makeRandomStr();
                writer.write(str);
            }
            writer.flush();

            startTime = System.nanoTime();
            while((r = reader.read()) != -1){
                reader.read();
            }
            endTime = System.nanoTime();
            System.out.println("일반 reader 사용: "+(endTime-startTime));

            startTime = System.nanoTime();
            while((r = reader.read(cbuf)) != -1){
                reader.read(cbuf);
            }
            endTime = System.nanoTime();
            System.out.println("cbuf reader 사용: cbuf길이: "+cbuf.length+" 시간: "+(endTime-startTime));

            startTime = System.nanoTime();

            while((line = br.readLine()) != null){

                br.readLine();
            }
            endTime = System.nanoTime();
            System.out.println("buffered reader 사용: "+(endTime-startTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static final int LENGTH = 24;
    private static String makeRandomStr() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i= 0;i<LENGTH;i++) {
            int randomIndex = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(randomIndex));
        }
        sb.append("\n");
        return sb.toString();
    }
}
```

```
일반 reader 사용: 78053300
cbuf reader 사용: cbuf길이: 8192 시간: 88300
buffered reader 사용: 71100
```

`cbuf`를 사용했을 때와 `BufferedReader`를 기본으로 사용했을때는 큰 차이는 없지만

일반 `reader`를 사용했을 때와는 큰 차이를 볼 수 있다.

### 배열은 어떻게 직렬화 하는걸까

자바에서 문자열을 입출력 하는 방법과 바이트 데이터를 입출력하는 방법은 각각 `OOOStream` 과 `OOOReader` 등으로 배웠다.

그러면 객체 자체를 보내는 방법은 뭘까?

이를 가능케 하기 위해서 자바에서는 객체의 데이터를 바이트화 시켜 전송하는 직렬화를 제공한다.

그런데 배열도 객체에 해당되는데, 그러면 `Serializable`을 어떻게 구현한걸까?

자바 리플렉션을 통해 배열도 클래스인지 검사할 수 있다.

```java
public static void main(String[] args) throws IOException {
    int [] arr = new int[]{10, 20, 30};
    arrayInfo(arr);
}
static void arrayInfo(Object arr){
    Class<?> clazz = arr.getClass();
    System.out.println("className: "+clazz.getName());
    System.out.println("isArray: "+clazz.isArray());
    Class<?> [] interfaces = clazz.getInterfaces();
    for(Class<?> inter: interfaces){
        System.out.println("implements: "+inter.getName());
    }
}
```

```
className: [I
isArray: true
implements: java.lang.Cloneable
implements: java.io.Serializable
```

클래스 이름이 뭔지 몰겠지만, 구현하고 있는 인터페이스에 `Serializable`이 있으므로 충분히 가능하다.

왜 자바에서 배열이 클래스인지는 더 알아봐야할듯
