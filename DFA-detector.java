import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

class DFA-detector{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String chars = null;
        String[] states = null;
        String startState = null;
        String[] endStates = null;
        HashMap<String, HashMap<String, String>> arcMap = new HashMap<>();

        /**
         * This try-catch will read DFA machine from file
         */
        try {
            File machineFile = new File("DFA_Input_1.txt");
            Scanner fileReader = new Scanner(machineFile);
            chars = fileReader.nextLine();
            states = fileReader.nextLine().split(" ");
            startState = fileReader.nextLine();
            endStates = fileReader.nextLine().split(" ");
            while(fileReader.hasNextLine()){
                String[] arc = fileReader.nextLine().split(" ");
                if(arcMap.containsKey(arc[0])){
                    arcMap.get(arc[0]).put(arc[1], arc[2]);
                } else {
                    arcMap.put(arc[0], new HashMap<String, String>());
                    arcMap.get(arc[0]).put(arc[1], arc[2]);
                }
            }
            fileReader.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        char[] input = scanner.nextLine().toCharArray();
        scanner.close();
        String curState = startState;

        //This for will process on input string
        for (char c : input) {
            String character = String.valueOf(c);
            if(!chars.contains(character) || !arcMap.keySet().contains(curState) || !arcMap.get(curState).keySet().contains(character)){
                System.out.println("This string is not valid");
                return;
            }
            curState = arcMap.get(curState).get(character);   
        }

        //This for will check the final state of input string
        for (String state : endStates) {
            if(state.equals(curState)){
                System.out.println("DFA accepts this string.");
                return;
            }
        }
        System.out.println("DFA doesn't accept this string");
    }
}