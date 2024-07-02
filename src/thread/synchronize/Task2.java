package thisisjava.thread.synchronize;

public class Task2 extends Thread{

    Memory memory;

    public Task2(String name){
        setName(name);
    }

    public void setMemory(Memory memory){
        this.memory = memory;
    }

    @Override
    public void run() {
        memory.blockWrite(50); // 싱크로 나이즈 블록이 포함된 메소드 호출
    }
}
