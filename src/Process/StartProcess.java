package Process;

public class StartProcess {


    public static void main(String[] args) throws InterruptedException {

        int sz = args.length;

        // check if we don't have the manager port so we can't send our port and communicate with the other processes
        int managerPort = 0;
        if (sz > 0) {
            managerPort = Integer.valueOf(args[0]);
        } else {
            // if there is no manager port in the args
            return;
        }

        Process process = new Process(ProcessHandle.current().pid() , managerPort);

    }


}
