import java.util.*;

class Process {
    int pid, burstTime, priority, remainingTime;

    Process(int pid, int burstTime, int priority) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }
}

class CircularQueue {
    Process[] queue;
    int front, rear, size, capacity;

    CircularQueue(int capacity) {
        this.capacity = capacity;
        queue = new Process[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    boolean isEmpty() {
        return size == 0;
    }

    boolean isFull() {
        return size == capacity;
    }

    void enqueue(Process p) {
        if (isFull()) {
            System.out.println("Queue is full!");
            return;
        }
        rear = (rear + 1) % capacity;
        queue[rear] = p;
        size++;
    }

    Process dequeue() {
        if (isEmpty())
            return null;

        Process p = queue[front];
        front = (front + 1) % capacity;
        size--;
        return p;
    }
}

public class cpu_Scheduling_Simulator {

    static LinkedList<Process> processes = new LinkedList<>();
    static Scanner sc = new Scanner(System.in);

    static void addProcess() {
        System.out.print("Enter Process ID: ");
        int id = sc.nextInt();

        System.out.print("Enter Burst Time: ");
        int burst = sc.nextInt();

        System.out.print("Enter Priority (1 = highest): ");
        int priority = sc.nextInt();

        processes.add(new Process(id, burst, priority));
        System.out.println("Process added successfully!");
    }

    static void displayProcesses() {
        if (processes.isEmpty()) {
            System.out.println("No processes available.");
            return;
        }

        System.out.println("\nProcess List:");
        for (Process p : processes) {
            System.out.println("PID: " + p.pid +
                    " | Burst Time: " + p.burstTime +
                    " | Priority: " + p.priority);
        }
    }

    // Sorting using Bubble Sort
    static void sortProcesses() {
        if (processes.isEmpty()) {
            System.out.println("No processes to sort.");
            return;
        }

        for (int i = 0; i < processes.size() - 1; i++) {
            for (int j = 0; j < processes.size() - i - 1; j++) {
                if (processes.get(j).burstTime >
                        processes.get(j + 1).burstTime) {

                    Process temp = processes.get(j);
                    processes.set(j, processes.get(j + 1));
                    processes.set(j + 1, temp);
                }
            }
        }

        System.out.println("Processes sorted by Burst Time!");
        displayProcesses();
    }

    // Priority Scheduling
    static void priorityScheduling() {
        if (processes.isEmpty()) {
            System.out.println("No processes available.");
            return;
        }

        PriorityQueue<Process> pq = new PriorityQueue<>(
            Comparator.comparingInt((Process p) -> p.priority)
                      .thenComparingInt(p -> p.pid)
        );

        pq.addAll(processes);

        System.out.println("\nPriority Scheduling:");
        while (!pq.isEmpty()) {
            Process p = pq.poll();
            System.out.println("Executing Process P" + p.pid +
                    " | Priority: " + p.priority);
        }
    }

    // Round Robin Scheduling using Circular Queue
    static void roundRobin() {
        if (processes.isEmpty()) {
            System.out.println("No processes available.");
            return;
        }

        System.out.print("Enter Time Quantum: ");
        int quantum = sc.nextInt();

        if (quantum <= 0) {
            System.out.println("Time quantum must be positive.");
            return;
        }

        CircularQueue cq = new CircularQueue(processes.size());

        // Create fresh copies for simulation
        for (Process p : processes) {
            cq.enqueue(new Process(p.pid, p.burstTime, p.priority));
        }

        System.out.println("\nRound Robin Scheduling:");
        while (!cq.isEmpty()) {
            Process p = cq.dequeue();

            int executionTime = Math.min(quantum, p.remainingTime);
            p.remainingTime -= executionTime;

            System.out.println("Process P" + p.pid +
                    " executed for " + executionTime + " units");

            if (p.remainingTime > 0) {
                cq.enqueue(p);
            } else {
                System.out.println("Process P" + p.pid + " completed.");
            }
        }
    }

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n===== CPU Scheduling Simulator =====");
            System.out.println("1. Add Process");
            System.out.println("2. Display Processes");
            System.out.println("3. Sort by Burst Time");
            System.out.println("4. Priority Scheduling");
            System.out.println("5. Round Robin Scheduling");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addProcess();
                    break;
                case 2:
                    displayProcesses();
                    break;
                case 3:
                    sortProcesses();
                    break;
                case 4:
                    priorityScheduling();
                    break;
                case 5:
                    roundRobin();
                    break;
                case 6:
                    System.out.println("Simulator closed.");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 6);
    }
}