import java.io.*;
import java.util.*;

public class Assignment4 {

    static Map<String, Integer> mnt = new LinkedHashMap<>();
    static List<String> mdt = new ArrayList<>();
    static Map<String, Map<String, String>> alaTable = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader mntReader = new BufferedReader(new FileReader("mnt.txt"));
        BufferedReader mdtReader = new BufferedReader(new FileReader("mdt.txt"));
        BufferedReader alaReader = new BufferedReader(new FileReader("ala.txt"));
        BufferedReader intermediateReader = new BufferedReader(new FileReader("macro_intermediate.txt"));
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter("expanded_code.txt"));

        // Load MNT
        String line;
        while ((line = mntReader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2) {
                mnt.put(parts[0], Integer.parseInt(parts[1]));
            }
        }

        // Load MDT
        while ((line = mdtReader.readLine()) != null) {
            mdt.add(line.trim());
        }

        // Load ALA Table
        String currentMacro = null;
        while ((line = alaReader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("ALA for ")) {
                currentMacro = line.substring(8, line.length() - 1).trim(); // remove "ALA for " prefix
                alaTable.put(currentMacro, new LinkedHashMap<>());
            } else if (!line.isEmpty() && currentMacro != null) {
                if (line.contains("->")) {
                    String[] parts = line.split("->");
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    alaTable.get(currentMacro).put(key, value);
                }
            }
        }

        // Read intermediate file and expand macros
        while ((line = intermediateReader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String macroName = parts[0];

            if (mnt.containsKey(macroName)) {
                int mdtIndex = mnt.get(macroName);
                String[] actualArgs = parts.length > 1 ? parts[1].split(",") : new String[0];

                // Get macro's ALA map from alaTable
                Map<String, String> positionalToFormal = alaTable.get(macroName); // #0 -> &ARG1
                Map<String, String> actualArgMap = new HashMap<>();

                // Build actualArgMap: #0 -> DATA1
                int i = 0;
                for (String key : positionalToFormal.keySet()) {
                    if (i < actualArgs.length) {
                        actualArgMap.put(key, actualArgs[i].trim());
                        i++;
                    }
                }

                // Expand macro from MDT
                for (int j = mdtIndex + 1; j < mdt.size(); j++) {
                    String mdtLine = mdt.get(j);
                    if (mdtLine.equalsIgnoreCase("MEND")) break;

                    for (Map.Entry<String, String> entry : actualArgMap.entrySet()) {
                        mdtLine = mdtLine.replace(entry.getKey(), entry.getValue());
                    }

                    outputWriter.write(mdtLine);
                    outputWriter.newLine();
                }
            } else {
                // Not a macro call
                outputWriter.write(line);
                outputWriter.newLine();
            }
        }

        // Close resources
        mntReader.close();
        mdtReader.close();
        alaReader.close();
        intermediateReader.close();
        outputWriter.close();

        System.out.println("✅ Macro Pass 2 complete. Output written to expanded_code.txt");
    }
}
