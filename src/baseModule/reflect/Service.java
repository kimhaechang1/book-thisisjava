package thisisjava.baseModule.reflect;


@Prettier(upString = "*", downString = "-")
public class Service {

    private Service(){}

    private static Service service;

    public static Service getInstance(){
        if(service == null){
            service = new Service();
        }
        return service;
    }

    public String getName(Member member){
        return member.getName();
    }

    public Integer getAge(Member member){
        return member.getAge();
    }
}
