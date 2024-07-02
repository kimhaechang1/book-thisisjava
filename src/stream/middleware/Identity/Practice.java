package thisisjava.stream.middleware.Identity;

import java.util.HashSet;

public class Practice {
    static class Data{
        // Data라는 클래스가 있다.
        public boolean equals(Object o){
            if(o == this) return true;
            if(o != null && getClass() != o.getClass()) return false;
            Data other = (Data) o;
            return true;
        }

        @Override
        public int hashCode() {
            return 1; // hashCode() 오버라이딩
        }
    }
    public static void main(String[] args) {
        Data obj1 = new Data(); // obj1이라는 Data 타입 인스턴스가 있다.
        Data obj2 = new Data();
        HashSet<Data> set = new HashSet<>();
        set.add(obj1);
        set.add(obj2);
        System.out.println(set.size() == 1); // 1개가 나오길 기대하는데 ...
    }
}
