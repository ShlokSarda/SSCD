import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;

public class Assignment1 {
	static Scanner in = new Scanner(System.in);
	static String is[] = { "STOP","ADD","SUB","MULT","MOVER","MOVEM","COMP","BC","DIV","READ","PRINT"};
	static String ad[] = { "START","END","ORIGIN","EQU","LTORG"};
	static String dl[] = { "DC" , "DS" };
	static String cc[] = { "LT","LE","EQ","GT","GE","ANY"};
	static int symcounter =0 ;
	// static int litcounter =0 ;
	static String sym[][] = new String[100][2];
	public static void main(String args[]) throws Exception{
		int locate=0;
		File file = new File("input.txt");
		File file1 = new File("intermediate.txt");
		File file2 = new File("symbol.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer= new BufferedWriter(new FileWriter(file1));
		BufferedWriter writer1= new BufferedWriter(new FileWriter(file2));
		String st;
		String sym_ans = "";
		String y,prev=null;
		int stp = 0;
		String ans;
		String buffer = "";
		System.out.println("Output Table : ");
		while((st = reader.readLine())!=null){
			int isflag=0;
			StringTokenizer splitted = new StringTokenizer(st);
			ans="";
			while(splitted.hasMoreTokens()){
				y = splitted.nextToken();
				if(y.equals("START")){
					locate = Integer.parseInt(splitted.nextToken());
					ans="(AD,01) (C,"+locate+")";
					break;
				}
				else{
					if(searchis(y)){
						if(y.equals("STOP")){
							stp=1;
						}
						ans+=locate+" (IS,0"+Integer.toString(indexis(y))+")";
						isflag=1;
						locate+=1;
					}
					else if(searchad(y)){
						if(y.equals("ORIGIN")){
							y=splitted.nextToken();
							String[] words = y.split("\\+");
							int location = Integer.parseInt(sym[indexsym(words[0])][1]);
							locate=location+Integer.parseInt(words[1]);
							ans=locate+" (AD,03) (S,"+Integer.toString(indexsym(words[0])+1)+")+"+words[1];
						}
						if(y.equals("EQU")){
							int temp = indexsym(splitted.nextToken());
							y=prev;
							sym[indexsym(y)][1]=  sym[temp][1];
							ans="";
						}
					}
					else if(searchdl(y)){
						if(y.equals("DC")){
							ans="";
							ans+=locate+" (DL,01) (C,"+splitted.nextToken()+")";
							locate+=1;
						}
						if(y.equals("DS")){
							ans="";
							String token;
							token=splitted.nextToken();
							System.out.println(token);
							ans+=locate+" (DL,02) (C,"+token+")";
							locate+=Integer.parseInt(token);;
						}
						
					}
					else{
						prev=y;
						if(y.equals("AREG")){
							ans+=" (1)";
						}
						else if(y.equals("BREG")){
							ans+=" (2)";
						}
						else if(y.equals("CREG")){
							ans+=" (3)";
						}
						else if(y.equals("DREG")){
							ans+=" (4)";
						}
						else if(searchcc(y)){
							ans+=" ("+Integer.toString(indexcc(y)+1)+")";
						}
						else{
							if(!searchsym(y) && isflag==0 && stp==0){
								sym[symcounter][0] = y;
								sym[symcounter++][1] = Integer.toString(locate);
								ans+=" (S,"+Integer.toString(indexsym(y)+1)+")";
								if(splitted.hasMoreTokens())
									ans="";
							}
							else if(!searchsym(y) && isflag==1 && stp==0 ){//if instruction has passed on the line then only add the symbol not the address
								sym[symcounter++][0] = y;
								ans+=" (S,"+Integer.toString(indexsym(y)+1)+")";
							}
							else if(searchsym(y) && isflag==0){
								sym[indexsym(y)][1]= Integer.toString(locate);
								ans+=" (S,"+Integer.toString(indexsym(y)+1)+")";
								if(splitted.hasMoreTokens())
									ans="";
								prev=y;
							}
							else{
								if(!splitted.hasMoreTokens())
									ans+=" (S,"+Integer.toString(indexsym(y)+1)+")";
								continue;   
							}
						}
					}
				}
			}
			ans=ans+"\n";
			buffer+=ans;
		}
		System.out.println(buffer+"\n");
		System.out.println("Symbol Table : ");
        System.out.printf("%-10s %-15s %-10s%n", "Sym_Id", "Value", "Address");  // Header with formatting
        System.out.println("---------------------------------------"); // Separator

		for (int i = 0; i < symcounter; i++) {
			sym_ans += String.format("%-10d %-15s %-10s\n", i + 1, sym[i][0], sym[i][1]);
			System.out.printf("%-10d %-15s %-10s%n", i + 1, sym[i][0], sym[i][1]);  // Formatted output
		}
        writer1.write(sym_ans);
		writer.write(buffer);
		reader.close();
		writer1.close();
		writer.close();
	}
	public static boolean searchis(String s){
		boolean flag = false;
		int i=0;
		while(i<11){
			if(is[i].equals(s)){
				flag=true;
				break;
			}
			i++;
		}
		return flag;
	}
	public static boolean searchad(String s){
		boolean flag = false;
		int i=0;
		while(i<5){
			if(ad[i].equals(s)){
				flag=true;
				break;
			}
			i++;
		}
		return flag;
	}
	public static boolean searchdl(String s){
		boolean flag = false;
		int i=0;
		while(i<2){
			if(dl[i].equals(s)){
				flag=true;
				break;
			}
			i++;
		}
		return flag;
	}
	public static boolean searchsym(String s){
		boolean flag = s.equals("BREG") || s.equals("AREG") || s.equals("CREG") || s.equals("DREG") || s.equals(",") ||s.equals("LE") || s.equals("LT") ||s.equals("ANY") ||s.equals("EQ") ||s.equals("GT") ||s.equals("GE");
		int i=0;
		while(i<symcounter ){
			if(sym[i][0].equals(s)){
				flag=true;
				break;
			}
			i++;
		}
		return flag;
	}
	public static boolean searchcc(String s){
		boolean flag = false;
		int i=0;
		while(i<6){
			if(cc[i].equals(s)){
				flag=true;
				break;
			}
			i++;
		}
		return flag;
	}
	public static int indexsym(String s){
		int c = 0;
		int i=0;
		while(i<symcounter){
			if(sym[i][0].equals(s)){
				c=i;
				break;
			}
			i++;
		}
		return i;
	}
	public static int indexis(String s){
		int i=0;
		while(i<11){
			if(is[i].equals(s)){
				break;
			}
			i++;
		}
		return i;
	}
	public static int indexad(String s){
		int i=0;
		while(i<5){
			if(ad[i].equals(s)){
				break;
			}
			i++;
		}
		return i;
	}
	public static int indexdl(String s){
		int i=0;
		while(i<2){
			if(dl[i].equals(s)){
				break;
			}
			i++;
		}
		return i;
	}
	public static int indexcc(String s){
		int i=0;
		while(i<6){
			if(cc[i].equals(s)){
				break;
			}
			i++;
		}
		return i;
	}
}