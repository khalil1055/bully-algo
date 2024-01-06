### Summary
- simulate the Bully election algorithm using any interprocess communication technology with multi-processes (without using threads)

### My assumptions ( not mentioned in the task )
- there is a manager to handle UI operations and create/kill the processes also handle the logs and configurations between the processes
- using peer to peer communication (client & server sockets to each process) to handle messages between them

### How it works
- run the jar file in the main directory or run the main class in the code
- select number of process and click create to run
- then the processes will be created and 
bully algorithm will start and coordinator will be elected 
and send alive message to other processes per a preset interval 
- can kill any process or kill the coordinator or add a new process
- you can see the logs to know the steps of the process

### Follow up ( would've implemented them if I had time )
- I was trying to make all things simple but in the end there is some complicated parts
- make the code more abstracted 
- better error handling
- better socket handling
- message part be more dynamic
- finish the todo parts in the code
- this list may be longer but i don't want to reinvent the wheel
