package servlet.jdbc;

public class TryWithResources {

    public static void main(String[] args) {

        MyAutoCloseable c1 = new MyAutoCloseable("c1");

        try (c1; MyAutoCloseable c2 = new MyAutoCloseable("c2")) {

            System.out.println("in try block");

        } catch (Exception e) {
            System.out.println("in catch block");
        }

        System.out.println("after try/catch block");
    }

    private static class MyAutoCloseable implements AutoCloseable {

        private String name;

        public MyAutoCloseable(String name) {
            this.name = name;
            System.out.println("creating MyAutoCloseable " + name);
        }

        @Override
        public void close() {
            System.out.println("closing MyAutoCloseable " + name);
        }

    }
}
