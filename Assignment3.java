import java.io.*;
import java.util.*;

public class Assignment3 {

    static List<String> mnt = new ArrayList<>();
    static List<String> mdt = new ArrayList<>();
    static Map<String, Integer> mntPointer = new LinkedHashMap<>();
    static Map<String, String> ala = new LinkedHashMap<>();
    static Map<String, Map<String, String>> alaTable = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("macro_input.txt"));
        BufferedWriter mntWriter = new BufferedWriter(new FileWriter("mnt.txt"));
        BufferedWriter mdtWriter = new BufferedWriter(new FileWriter("mdt.txt"));
        BufferedWriter interWriter = new BufferedWriter(new FileWriter("macro_intermediate.txt"));
        BufferedWriter alaWriter = new BufferedWriter(new FileWriter("ala.txt"));

        String line;
        boolean isMacroDef = false;
        String currentMacro = "";
        int mdtIndex = 0;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equalsIgnoreCase("MACRO")) {
                isMacroDef = true;
                ala.clear();
                continue;
            }

            if (isMacroDef) {
                if (currentMacro.isEmpty()) {
                    // Get macro name and parameters
                    
                    String[] parts = line.split(" ");
                    currentMacro = parts[0];
                    mntPointer.put(currentMacro, mdtIndex);
                    mnt.add(currentMacro);
                    mdt.add(line);
                    mdtIndex++;
                    if (parts.length > 1) {
                        String[] argsList = parts[1].split(",");
                        for (int i = 0; i < argsList.length; i++) {
                            ala.put(argsList[i], "#" + i);
                        }
                    }
                    // Store ALA per macro
                    alaTable.put(currentMacro, new LinkedHashMap<>(ala));
                } else if (!line.equalsIgnoreCase("MEND")) {
                    for (Map.Entry<String, String> entry : ala.entrySet()) {
                        line = line.replace(entry.getKey(), entry.getValue());
                    }
                    mdt.add(line);
                    mdtIndex++;
                } else {
                    mdt.add("MEND");
                    mdtIndex++;
                    isMacroDef = false;
                    currentMacro = "";
                }
            } else {
                interWriter.write(line);
                interWriter.newLine();
            }
        }

        // Write MNT
        for (String macro : mnt) {
            mntWriter.write(macro + " " + mntPointer.get(macro));
            mntWriter.newLine();
        }

        // Write MDT
        for (String def : mdt) {
            mdtWriter.write(def);
            mdtWriter.newLine();
        }

        // Write ALA
        for (String macro : alaTable.keySet()) {
            alaWriter.write("ALA for " + macro + ":\n");
            Map<String, String> args1 = alaTable.get(macro);
            for (Map.Entry<String, String> entry : args1.entrySet()) {
                alaWriter.write(entry.getValue() + " -> " + entry.getKey() + "\n");
            }
            alaWriter.write("\n");
        }

        br.close();
        mntWriter.close();
        mdtWriter.close();
        alaWriter.close();
        interWriter.close();

        System.out.println("Macro Pass 1 Complete. MNT, MDT, ALA, and intermediate file generated.");
    }
}
