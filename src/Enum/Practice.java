package thisisjava.Enum;

public class Practice {
    public static void main(String[] args) {
        Week week = Week.MONDAY;
        Week week1 = Week.MONDAY;
        System.out.println(week);
        System.out.println(week == week1);
        System.out.println(week.name());
//        System.out.println(week.name);
//        System.out.println(week.getIndex());
    }
}
