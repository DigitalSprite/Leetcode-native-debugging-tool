public class Main {
    public static void main(String[] args) {
        if (args[0].equals("generate")) {
            FileGenerator.createClass();
        } else if (args[0].equals("run")) {
            Runner runner = new Runner();
            runner.run();
        }
    }
}
