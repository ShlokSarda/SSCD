import java.io.*;
import java.util.*;

public class Assignment2 {

    static Map<Integer, String> symbolTable = new HashMap<>();

    public static void main(String[] args) throws IOException {
        loadSymbolTable("symbol.txt");

        BufferedReader br = new BufferedReader(new FileReader("intermediate.txt"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("MachineCode.txt"));

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            int lc = -1;
            if (Character.isDigit(line.charAt(0))) {
                int spaceIndex = line.indexOf(' ');
                lc = Integer.parseInt(line.substring(0, spaceIndex));
                line = line.substring(spaceIndex + 1).trim();
            }

            String machineCode = generateMachineCode(line);

            if (!machineCode.isEmpty() && lc != -1) {
                bw.write(lc + " " + machineCode);
                bw.newLine();
            }
        }

        br.close();
        bw.close();

        System.out.println("Pass 2 complete. Output written to MachineCode.txt");
    }

    static void loadSymbolTable(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length < 3) {
                System.out.println("Skipping invalid line in symbol table: " + line);
                continue;
            }

            int index = Integer.parseInt(parts[0]);
            String address = parts[2];
            symbolTable.put(index, address);
            System.out.println("Loaded symbol: index=" + index + ", address=" + address);
        }
        br.close();
    }

    static String generateMachineCode(String line) {
        StringBuilder result = new StringBuilder();

        String[] tokens = line.split(" ");
        String opcode = "", reg = "0", memAddr = "000";

        for (String rawToken : tokens) {
            String token = rawToken.replaceAll("[()]", "");

            if (token.startsWith("IS,")) {
                opcode = padConstant(token.split(",")[1]);
                System.out.println("Opcode: " + opcode);
            } else if (token.matches("\\d+")) {
                reg = token;
                System.out.println("Register: " + reg);
            } else if (token.startsWith("S,")) {
                int symIndex = Integer.parseInt(token.split(",")[1]);
                memAddr = symbolTable.getOrDefault(symIndex, "000");
                System.out.println("Symbol index " + symIndex + " resolved to " + memAddr);
            } else if (token.startsWith("C,")) {
                String constant = token.split(",")[1];
                memAddr = padConstant(constant);
                System.out.println("Constant: " + memAddr);
            } else if (token.startsWith("DL,")) {
                    return "000 0 000"; // DS or other DL - skip
            }
        }

        if (!opcode.isEmpty()) {
            result.append(opcode).append(" ").append(reg).append(" ").append(memAddr);
        }

        return result.toString().trim();
    }

    static String padConstant(String value) {
        return String.format("%03d", Integer.parseInt(value));
    }
}
