import java.io.*;

class Main {
    public static void main(String[] args) throws IOException {
        // File paths
        String landFile = args[0];
        String travelTimeFile =  args[1];
        String missionFile = args[2];
        String outputFile =  args[3];

        // Measure start time
        long startTime = System.nanoTime();

        // Parse inputs
        Functions.parseInputs(landFile, travelTimeFile, missionFile);

        // Simulate the journey
        Functions.simulate();

        // Write outputs
        Functions.writeOutput(outputFile);

        // Measure end time
        long endTime = System.nanoTime();

        // Calculate elapsed time in seconds
        double elapsedTimeInSeconds = (endTime - startTime) / 1_000_000_000.0;

        // Print runtime to console
        System.out.printf("Execution Time: %.3f seconds%n", elapsedTimeInSeconds);
    }
}
