import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;

class Question2 {
    public static String chars = null;
    public static String[] states = null;
    public static String startState = null;
    public static String[] endStates = null;
    public static HashMap<String, HashMap<String, String>> arcMap = new HashMap<>();
    public static HashMap<String, HashMap<String, String>> dfaArcMap = new HashMap<>();
    public static HashMap<String, String> map = new HashMap<>();

    public static void main(String[] args) {
        load();
        //start of conversion process
        conversion(nullClosure(startState));
        
        makeResult();

        save();
    }
    
    /**
     * This method will read NFA machine from file
     */
    private static void load(){
        try {
            File machineFile = new File("NFA_Input_2.txt");
            Scanner fileReader = new Scanner(machineFile);
            chars = fileReader.nextLine();
            states = fileReader.nextLine().split(" ");
            startState = fileReader.nextLine();
            endStates = fileReader.nextLine().split(" ");
            while(fileReader.hasNextLine()){
                String[] arc = fileReader.nextLine().split(" ");
                if(arcMap.containsKey(arc[0])){
                    if(arcMap.get(arc[0]).keySet().contains(arc[1])){
                        arcMap.get(arc[0]).put(arc[1], arcMap.get(arc[0]).get(arc[1]) + " " + arc[2]);
                    } else {
                        arcMap.get(arc[0]).put(arc[1], arc[2]);
                    }
                } else {
                    arcMap.put(arc[0], new HashMap<String, String>());
                    arcMap.get(arc[0]).put(arc[1], arc[2]);
                }
            }
            fileReader.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * THis method will save converted NFA machine into file
     */
    private static void save(){
        try{
            File file = new File("DFA_Output_2.txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(chars + "\n");
            for (String string : map.values()) {
                fileWriter.write(string + " ");
            }
            fileWriter.write("\n");
            fileWriter.write(map.get(nullClosure(startState)) + "\n");
            String dfaEnds = "";
            for (String ends : endStates) {
                for (String state : map.keySet()) {
                    if(state.contains(ends) && !dfaEnds.contains(state)){
                        dfaEnds = dfaEnds + " " + map.get(state);
                    }
                }
            }
            fileWriter.write(dfaEnds.trim() + "\n");
            for (String state : dfaArcMap.keySet()) {
                for (String head : chars.split(" ")) {
                    if(!isNullOrEmpty(map.get(dfaArcMap.get(state).get(head)))){
                        fileWriter.write(map.get(state) + " " + head + " " + map.get(dfaArcMap.get(state).get(head)) + "\n");
                    }
                }
            }
            fileWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method will make a hashmap of renamed state on DFA machine
     */
    private static void makeResult(){
        int count = 0;
        for (String state : dfaArcMap.keySet()) {
            if(!isNullOrEmpty(state) && !map.containsKey(state)){
                map.put(state, "s" + count);
                count++;
            }
            for (String head : chars.split(" ")) {
                boolean isRepeative = false;
                for (String subState : map.keySet()) {
                    if(equalState(subState, dfaArcMap.get(state).get(head))){
                        dfaArcMap.get(state).replace(head, subState);
                        isRepeative = true;
                        break;
                    }
                }
                if(!isRepeative){
                    if(!isNullOrEmpty(dfaArcMap.get(state).get(head))){
                        map.put(dfaArcMap.get(state).get(head), "s" + count);
                        count++;
                    }
                }
            }
        }
    }

    /**
     * Return transitions of a specific state with a head
     */
    private static String arcFinder(String state, String readHead){
        String finalStr = "";
        for (String subState : state.split(" ")) {
            if(arcMap.keySet().contains(subState)){
                if(arcMap.get(subState).keySet().contains(readHead)){
                    finalStr = unionStates(finalStr, arcMap.get(subState).get(readHead));
                } else {
                    finalStr = unionStates(finalStr, "");
                }
            } else {
                finalStr = unionStates(finalStr, "");
            }
        }
        return finalStr.trim();
    }

    /**
     * Returns nullClosure of a state
     * @param state
     * @return
     */
    private static String nullClosure(String state){
        String nullCloString = state;
        for (String subState : state.split(" ")) {
            if(arcMap.keySet().contains(subState)){
                if(arcMap.get(subState).keySet().contains("~")){
                    nullCloString = unionStates(nullCloString, nullClosure(arcMap.get(subState).get("~")));
                }
            }
        }
        return nullCloString.trim();
    }

    /**
     * Returns a union of two state in string format
     * @param firstStr
     * @param secondStr
     * @return
     */
    private static String unionStates(String firstStr, String secondStr){
        firstStr = firstStr.trim();
        secondStr = secondStr.trim();

        String finalStr = "";
        String[] firstArr = firstStr.split(" ");
        String[] secondArr = secondStr.split(" ");

        for(int i = 0; i < firstArr.length; i++){
            if(!finalStr.contains(firstArr[i])){
                finalStr = finalStr + " " + firstArr[i];
            }
        }

        for(int i = 0; i < secondArr.length; i++){
            if(!finalStr.contains(secondArr[i])){
                finalStr = finalStr + " " + secondArr[i];
            }
        }
        
        return finalStr.trim();
    }

    /**
     * This method is main method of NFA to DFA convert
     * @param state
     */
    private static void conversion(String state){
        dfaArcMap.put(state, new HashMap<String, String>());
        for (String head : chars.split(" ")) {
            String target = nullClosure(arcFinder(state, head));
            if(!isNullOrEmpty(target)){
                dfaArcMap.get(state).put(head, target);
                boolean isRepeative = false;
                for (String subState : dfaArcMap.keySet()) {
                    if(equalState(subState, target)){
                        isRepeative = true;
                        break;
                    }
                }
                if(!isRepeative){
                    conversion(target);
                }
            }
        }
    }

    /**
     * This method will check two states to show if they are equal or not
     * @param firstStr
     * @param secStr
     * @return true for equality and false for inequality
     */
    private static boolean equalState(String firstStr, String secStr){
        String[] firstArr = firstStr.split(" ");
        String[] secArr = null;
        try{
            secArr = secStr.split(" ");
        } catch (NullPointerException e){
            return false;
        }
        if(firstArr.length != secArr.length){
            return false;
        } else {
            for (String state : firstArr) {
                if(!secStr.contains(state)){
                    return false;
                }
            }
            return true;
        }
    }


    private static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}