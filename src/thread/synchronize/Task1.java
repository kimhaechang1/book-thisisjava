package thisisjava.thread.synchronize;

public class Task1 extends Thread{

    Memory memory;

    public Task1(String name){
        setName(name);
    }

    public void setMemory(Memory memory){
        this.memory = memory;
    }

    @Override
    public void run() {
        memory.blockWrite(100); // 싱크로 나이즈 메소드 호출
    }
}
