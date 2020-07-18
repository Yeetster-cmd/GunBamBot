package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;


public class Command extends BotListener{

	public static void nextMonth(TextChannel tc, Message msg) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
		
		//��
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
		
		//��
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int datey = Integer.parseInt(sdf3.format(date));
	
		int moncount = 0;
		
		if(datem == 1 ) {moncount = 31;}
		else if(datem == 2 ) {
			if(datey%4 == 0) {moncount = 29;}
			else {moncount = 28;}
		}
		else if(datem == 3) {moncount = 31;}
		else if(datem == 4) {moncount = 30;}
		else if(datem == 5) {moncount = 31;}
		else if(datem == 6) {moncount = 30;}
		else if(datem == 7) {moncount = 31;}
		else if(datem == 8) {moncount = 31;}
		else if(datem == 9) {moncount = 30;}
		else if(datem == 10) {moncount = 31;}
		else if(datem == 11) {moncount = 30;}
		else if(datem == 12) {moncount = 31;}
		
	
		int result = moncount - dated + 1;
		
		tc.sendMessage("**���� �ޱ��� " + result + "��** ���Ҿ��").queue();
		System.out.println("BOT: **���� �ޱ��� " + result + "��** ���Ҿ��");
	}
	
	public static void today(TextChannel tc, Message msg) {
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
		
		//��
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
		
		//��
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int datey = Integer.parseInt(sdf3.format(date));
		
		tc.sendMessage("������ **" + datey + "�� " + datem + "�� " + dated + "��** �̿���").queue();
		System.out.println("BOT: ������ **" + datey + "�� " + datem + "�� " + dated + "��** �̿���");
	}
	
	public static void untilDay(TextChannel tc, Message msg) {
		Date date = new Date();
    	String recog = msg.getContentRaw().replaceAll("[^0-9]", "");
    	int recogint = 0;
    	
    	if(msg.getContentRaw().contains("����")) {
    
			calculateLeftDay(tc, date, 11, 19, 0);	  
		 }
    	
    	
    	else if(msg.getContentRaw().contains("10��")||msg.getContentRaw().contains("11��")||msg.getContentRaw().contains("12��")) {
			 if(msg.getContentRaw().contains("��")) {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    int year = 0;
					    	
					    if(recogint < 10000000) {
					    	year = recogint/1000;
					    	mon = (recogint - year*1000) / 10;
					    	day = (recogint - year*1000) % 10;
					    } //10�� �Ǳ� ������ �� �и� 
					    	
					    else {
					    	year = recogint/10000;
					    	mon = (recogint - year*10000) / 100;
					    	day = (recogint - year*10000) % 100;
					    } // 10�� �� ���� �� �и�
					    
					    calculateLeftDay(tc, date, mon, day, year);
				    }
				    
				    else {
				    	pardon(tc);
				    }
			 }
			 
			 else {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    	
					    if(recogint < 1000) {
					    	mon = recogint/10;
					    	day = recogint%10;
					    } //10�� �Ǳ� ������ �� �и� 
					    	
					    else {
					    	mon = recogint/100;
					    	day = recogint%100;
					    } // 10�� �� ���� �� �и�
					    
					    calculateLeftDay(tc, date, mon, day, 0);
				    }
				    
				    else {
				    	pardon(tc);
				    }
			 }
			   
		 }
	
    	else if(msg.getContentRaw().contains("1��")||msg.getContentRaw().contains("2��")||msg.getContentRaw().contains("3��")||msg.getContentRaw().contains("4��")||msg.getContentRaw().contains("5��")||msg.getContentRaw().contains("6��")||msg.getContentRaw().contains("7��")||msg.getContentRaw().contains("8��")||msg.getContentRaw().contains("9��")) {
    		if(msg.getContentRaw().contains("��")) {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    int year = 0;
					    	
					    if(recogint < 1000000) {
					    	year = recogint/100;
					    	mon = (recogint - year*100) / 10;
					    	day = (recogint - year*100) % 10;
					    } //10�� �Ǳ� ������ �� �и� 
					    	
					    else {
					    	year = recogint/1000;
					    	mon = (recogint - year*1000) / 100;
					    	day = (recogint - year*1000) % 100;
					    } // 10�� �� ���� �� �и�
					    
					    calculateLeftDay(tc, date, mon, day, year);
				    }
				    
				    else {
				    	pardon(tc);
				    }
			}
    		
    		else {
			    if(isNumber(recog) == true) {
				    recogint = Integer.parseInt(recog);
				    int mon = 0;
				    int day = 0;
				    	
				    if(recogint < 100) {
				    	mon = recogint/10;
				    	day = recogint%10;
				    } //10�� �Ǳ� ������ �� �и� 
				    	
				    else {
				    	mon = recogint/100;
				    	day = recogint%100;
				    } // 10�� �� ���� �� �и�
				    
				    calculateLeftDay(tc, date, mon, day, 0);
			    }
			    
			    else {
			    	pardon(tc);
			    }
    		}   
		}
	
		else {
			
	    	
	    	if(isNumber(recog) == true) {
	    		
	    		recogint = Integer.parseInt(recog);
	    		
	        	SimpleDateFormat sdf = new SimpleDateFormat("dd");
	    		int dated = Integer.parseInt(sdf.format(date));
	    		
	    		//��
	    		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
	    		int datem = Integer.parseInt(sdf2.format(date));
	    		
	    		//��
	    		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
	    		int datey = Integer.parseInt(sdf3.format(date));
	
	    		int moncount = 0;
	    		
	    		if(datem == 1 ) {moncount = 31;}
	    		else if(datem == 2 ) {
	    			if(datey%4 == 0) {moncount = 29;}
	    			else {moncount = 28;}
	    		}
	    		else if(datem == 3) {moncount = 31;}
	    		else if(datem == 4) {moncount = 30;}
	    		else if(datem == 5) {moncount = 31;}
	    		else if(datem == 6) {moncount = 30;}
	    		else if(datem == 7) {moncount = 31;}
	    		else if(datem == 8) {moncount = 31;}
	    		else if(datem == 9) {moncount = 30;}
	    		else if(datem == 10) {moncount = 31;}
	    		else if(datem == 11) {moncount = 30;}
	    		else if(datem == 12) {moncount = 31;}
	    		
	    		if(recogint < dated) {recogint = recogint + moncount;}
	    	
	    		int result = recogint - dated;
	    		
	    		tc.sendMessage("**" + recog + "�ϱ��� " + result + "��** ���Ҿ��").queue();
	    		System.out.println("BOT: **" + recog + "�ϱ��� " + result + "��** ���Ҿ��");
	    	}
	    	else {
	    		Command.pardon(tc);
	    	}
		}
	}
	
	public static void lastDay(TextChannel tc, Message msg) {
		Date date = new Date();
    	String recog = msg.getContentRaw().replaceAll("[^0-9]", "");
    	int recogint = 0;
    	
    	if(msg.getContentRaw().contains("10��")||msg.getContentRaw().contains("11��")||msg.getContentRaw().contains("12��")) {
    		if(msg.getContentRaw().contains("��")) {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    int year = 0;
					    	
					    if(recogint < 10000000) {
					    	year = recogint/1000;
					    	mon = (recogint - year*1000) / 10;
					    	day = (recogint - year*1000) % 10;
					    } //10�� �Ǳ� ������ �� �и� 
					    	
					    else {
					    	year = recogint/10000;
					    	mon = (recogint - year*10000) / 100;
					    	day = (recogint - year*10000) % 100;
					    } // 10�� �� ���� �� �и�
					    
					    int mo = mon;
					    int da = day;
					    int yea = year;
					    Runnable cal = () -> {
					    	calculateLastDay(tc, date, mo, da, yea);
					    };
					    Thread t = new Thread(cal);
					    t.start();
				    }
				    
				    else {
				    	pardon(tc);
				    }
			 }
    		
    		 else {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    	
					    if(recogint < 1000) {
					    	mon = recogint/10;
					    	day = recogint%10;
					    } //10�� �Ǳ� ������ �� �и� 
					    	
					    else {
					    	mon = recogint/100;
					    	day = recogint%100;
					    } // 10�� �� ���� �� �и�
					    
					    int mo = mon;
					    int da = day;
					   
					    Runnable cal = () -> {
					    	calculateLastDay(tc, date, mo, da, 0);
					    };
					    Thread t = new Thread(cal);
					    t.start();
					    
				    }
				    
				    else {
				    	pardon(tc);
				    }
    		 }
		 }
	
    	else if(msg.getContentRaw().contains("1��")||msg.getContentRaw().contains("2��")||msg.getContentRaw().contains("3��")||msg.getContentRaw().contains("4��")||msg.getContentRaw().contains("5��")||msg.getContentRaw().contains("6��")||msg.getContentRaw().contains("7��")||msg.getContentRaw().contains("8��")||msg.getContentRaw().contains("9��")) {
    		if(msg.getContentRaw().contains("��")) {
				 if(isNumber(recog) == true) {
					    recogint = Integer.parseInt(recog);
					    int mon = 0;
					    int day = 0;
					    int year = 0;
					    	
					    if(recogint < 1000000) {
					    	year = recogint/100;
					    	mon = (recogint - year*100) / 10;
					    	day = (recogint - year*100) % 10;
					    } //10�� �Ǳ� ������ �� �и� 
					    	
					    else {
					    	year = recogint/1000;
					    	mon = (recogint - year*1000) / 100;
					    	day = (recogint - year*1000) % 100;
					    } // 10�� �� ���� �� �и�
					    
					    
					    int mo = mon;
					    int da = day;
					    int yea = year;
					    
					    Runnable cal = () -> {
					    	calculateLastDay(tc, date, mo, da, yea);
					    };
					    Thread t = new Thread(cal);
					    t.start();
					    
				    }
				    
				    else {
				    	pardon(tc);
				    }
			}
    		
    		else {
			    if(isNumber(recog) == true) {
				    recogint = Integer.parseInt(recog);
				    int mon = 0;
				    int day = 0;
				    	
				    if(recogint < 100) {
				    	mon = recogint/10;
				    	day = recogint%10;
				    } //10�� �Ǳ� ������ �� �и� 
				    	
				    else {
				    	mon = recogint/100;
				    	day = recogint%100;
				    } // 10�� �� ���� �� �и�
				    
				    int mo = mon;
				    int da = day;
				    
				    Runnable cal = () -> {
				    	calculateLastDay(tc, date, mo, da, 0);
				    };
				    Thread t = new Thread(cal);
				    t.start();
  
			    }
			    
			    else {
			    	pardon(tc);
			    }
    		}
		    
		    
		}
	
		else {
	    	tc.sendMessage("**������** ����� �ּ���").queue();
	    	System.out.println("BOT: **������** ����� �ּ���");
		}
	}
	
	public static void pardon(TextChannel tc) {
		
		extend = 1;
		Random r = new Random();
		int i = r.nextInt(2);
		if(i == 0) { 
			tc.sendMessage("��?").queue();
			System.out.println("BOT: ��?");
		}
		
		else {
			tc.sendMessage("��?").queue();
			System.out.println("BOT: ��?");
		}
			
	}
	
	public static void dateCount(int plusint, TextChannel tc) {
		
		boolean run = true;
		
		Date date = new Date();
		
		//��
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
		
		//��
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
		
		//��
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int datey = Integer.parseInt(sdf3.format(date));
		int year = Integer.parseInt(sdf3.format(date));
		
		int result = plusint + dated;
		
		int moncount = 0;
		
		if(datem == 1 ) {moncount = 31;}
		else if(datem == 2 ) {
			if(datey%4 == 0) {moncount = 29;}
			else {moncount = 28;}
		}
		else if(datem == 3) {moncount = 31;}
		else if(datem == 4) {moncount = 30;}
		else if(datem == 5) {moncount = 31;}
		else if(datem == 6) {moncount = 30;}
		else if(datem == 7) {moncount = 31;}
		else if(datem == 8) {moncount = 31;}
		else if(datem == 9) {moncount = 30;}
		else if(datem == 10) {moncount = 31;}
		else if(datem == 11) {moncount = 30;}
		else if(datem == 12) {moncount = 31;}

		while(run) {
			if(datem == 1 ) {moncount = 31;}
			else if(datem == 2 ) {
				if(datey%4 == 0) {moncount = 29;}
				else {moncount = 28;}
			}
			else if(datem == 3) {moncount = 31;}
			else if(datem == 4) {moncount = 30;}
			else if(datem == 5) {moncount = 31;}
			else if(datem == 6) {moncount = 30;}
			else if(datem == 7) {moncount = 31;}
			else if(datem == 8) {moncount = 31;}
			else if(datem == 9) {moncount = 30;}
			else if(datem == 10) {moncount = 31;}
			else if(datem == 11) {moncount = 30;}
			else if(datem == 12) {moncount = 31;}
			
			if(result<moncount) {
				run = false;
				break;
			
			}
			
			else {
				result = result - moncount;
					
				datem = datem + 1;
			}
			
			if(datem > 12) {
				datey = datey + 1;
				datem = datem - 12;
			}
	
		}

		String y;
		if(year == datey) {y = "";}
		else {y = String.valueOf(datey) + "�� ";}
		
		tc.sendMessage("**" + plusint + "�� �ڴ� " + y + datem + "�� " + result + "��** �̿���").queue();
		System.out.println("BOT: **" + plusint + "�� �ڴ� " + y + datem + "�� " + result + "��** �̿���");
		
	}
	
	public static void dateCountMinus(int minusint, TextChannel tc) {
		
		boolean run = true;
		
		Date date = new Date();
		
		//��
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
		
		//��
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
		
		//��
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int datey = Integer.parseInt(sdf3.format(date));
		int year = Integer.parseInt(sdf3.format(date));
		

		int moncount = 0;
		
		if(datem == 1 ) {moncount = 31;}
		else if(datem == 2 ) {
			if(datey%4 == 0) {moncount = 29;}
			else {moncount = 28;}
		}
		else if(datem == 3) {moncount = 31;}
		else if(datem == 4) {moncount = 30;}
		else if(datem == 5) {moncount = 31;}
		else if(datem == 6) {moncount = 30;}
		else if(datem == 7) {moncount = 31;}
		else if(datem == 8) {moncount = 31;}
		else if(datem == 9) {moncount = 30;}
		else if(datem == 10) {moncount = 31;}
		else if(datem == 11) {moncount = 30;}
		else if(datem == 12) {moncount = 31;}
		
		int result = dated - minusint;
		
		while(run) {
			if(datem == 2 ) {moncount = 31;}
			else if(datem == 3 ) {
				if(datey%4 == 0) {moncount = 29;}
				else {moncount = 28;}
			}
			else if(datem == 4) {moncount = 31;}
			else if(datem == 5) {moncount = 30;}
			else if(datem == 6) {moncount = 31;}
			else if(datem == 7) {moncount = 30;}
			else if(datem == 8) {moncount = 31;}
			else if(datem == 9) {moncount = 31;}
			else if(datem == 10) {moncount = 30;}
			else if(datem == 11) {moncount = 31;}
			else if(datem == 12) {moncount = 30;}
			else if(datem == 1) {moncount = 31;}
			
			if(result<1) {
				result = result + moncount;
				
				datem = datem - 1;
			}
			
			if(datem < 1) {
				datey = datey - 1;
				datem = datem + 12;
			}
			
			if(result > 0) {
				run = false;
				break;
			}

		}
		
		String y;
		if(year == datey) {y = "";}
		else {y = String.valueOf(datey) + "�� ";}
		
		tc.sendMessage("**" + minusint + "�� ���� " + y + datem + "�� " + result + "��** �̾����").queue();
		System.out.println("BOT: **" + minusint + "�� ���� " + y + datem + "�� " + result + "��** �̾����");
		
	}
	
	public static void calculateLeftDay(TextChannel tc, Date date, int mon, int day, int yearInt) {
		
		boolean run = true;
		
		if(mon > 12) {
			tc.sendMessage("���� �ȵſ�").queue();
			return;
		}
		
		if(day > 31) {
			tc.sendMessage("���� �ȵſ�").queue();
			return;
		}
		
		//��
	    SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int dated = Integer.parseInt(sdf.format(date));
			
		//��
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int datem = Integer.parseInt(sdf2.format(date));
			
		//��
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int year = Integer.parseInt(sdf3.format(date));
		int datey = Integer.parseInt(sdf3.format(date));

	
		int moncount = 0;
			
		if(datem == 1 ) {moncount = 31;}
		else if(datem == 2 ) {
			if(datey%4 == 0) {moncount = 29;}
			else {moncount = 28;}
		}
		else if(datem == 3) {moncount = 31;}
		else if(datem == 4) {moncount = 30;}
		else if(datem == 5) {moncount = 31;}
		else if(datem == 6) {moncount = 30;}
		else if(datem == 7) {moncount = 31;}
		else if(datem == 8) {moncount = 31;}
		else if(datem == 9) {moncount = 30;}
		else if(datem == 10) {moncount = 31;}
		else if(datem == 11) {moncount = 30;}
		else if(datem == 12) {moncount = 31;}
			
		int monInt = mon;
		int dayInt = day;
		int yearCount = 0;
		
		int dayResult = dayInt - dated;
		int result = 0;
		
		if(dayInt <= dated) {
			if(datem == monInt) {
				year = year + 1;
			}
		}
		
		if(yearInt > 0) {
			yearCount = yearInt - year;

		}
		
		if(yearCount < 0) {
			tc.sendMessage("���� �ȵſ�").queue();
			System.out.println("BOT: ���� �ȵſ�");
	
			return;
		}

		while(run) {
				
			if(datem == 1 ) {moncount = 31;}
			else if(datem == 2 ) {
				if(datey%4 == 0) {moncount = 29;}
				else {moncount = 28;}
			}
			else if(datem == 3) {moncount = 31;}
			else if(datem == 4) {moncount = 30;}
			else if(datem == 5) {moncount = 31;}
			else if(datem == 6) {moncount = 30;}
			else if(datem == 7) {moncount = 31;}
			else if(datem == 8) {moncount = 31;}
			else if(datem == 9) {moncount = 30;}
			else if(datem == 10) {moncount = 31;}
			else if(datem == 11) {moncount = 30;}
			else if(datem == 12) {moncount = 31;}
				
			if(datem > 12) {
				datey = datey + 1;
				datem = datem - 12;
			}

	
			if(datey == year && datem >= monInt) {	
				run = false;
				break;
			}

			else {
				result = result + moncount;
				datem = datem + 1;
			}
		}
		result = result + dayResult;
		
		

		
		for(int i=0; i<yearCount; i++) {
				
			for(int j=0; j<12; j++) {
				if(datem > 12) {
					datey = datey + 1;
					datem = datem - 12;
				}
	
				if(datem == 1 ) {moncount = 31;}
				else if(datem == 2 ) {
					if(datey%4 == 0) {moncount = 29;}
					else {moncount = 28;}
				}
				else if(datem == 3) {moncount = 31;}
				else if(datem == 4) {moncount = 30;}
				else if(datem == 5) {moncount = 31;}
				else if(datem == 6) {moncount = 30;}
				else if(datem == 7) {moncount = 31;}
				else if(datem == 8) {moncount = 31;}
				else if(datem == 9) {moncount = 30;}
				else if(datem == 10) {moncount = 31;}
				else if(datem == 11) {moncount = 30;}
				else if(datem == 12) {moncount = 31;}
						
				result = result + moncount;
				datem = datem + 1;
			}
			
		}
		
		datey = Integer.parseInt(sdf3.format(date));
	
		String y;
		if(year == datey) {y = "";}
		else {y = String.valueOf(year) + "�� ";}
		
		if(yearInt > 0) {
			tc.sendMessage("**" + yearInt + "�� " + mon + "�� " + day + "�ϱ��� " + result + "��** ���Ҿ��").queue();
			System.out.println("BOT: **" + yearInt + "�� " + mon + "�� " + day + "�ϱ��� " + result + "��** ���Ҿ��");

		}
		else {
			tc.sendMessage("**" + y + mon + "�� " + day + "�ϱ��� " + result + "��** ���Ҿ��").queue();
			System.out.println("BOT: **" + y + mon + "�� " + day + "�ϱ��� " + result + "��** ���Ҿ��");
		}
	}
	
	public static void calculateLastDay(TextChannel tc, Date date, int mon, int day, int yearInt) {
		
		boolean run = true;
		
		if(mon > 12) {
			tc.sendMessage("���� �ȵſ�").queue();
			return;
		}
		
		if(day > 31) {
			tc.sendMessage("���� �ȵſ�").queue();
			return;
		}
		
		//��
	    SimpleDateFormat sdf = new SimpleDateFormat("dd");
		int nowDay = Integer.parseInt(sdf.format(date));
			
		//��
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		int nowMonth = Integer.parseInt(sdf2.format(date));
			
		//��
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		int nowYear = Integer.parseInt(sdf3.format(date));
		
		int startDay = day;
		int startMonth = mon;
		int startYear = yearInt;
		
		int dayCount = 0;

		/*
		if(datem == 1 ) {moncount = 31;}
		else if(datem == 2 ) {
			if(year%4 == 0) {moncount = 29;}
			else {moncount = 28;}
		}
		else if(datem == 3) {moncount = 31;}
		else if(datem == 4) {moncount = 30;}
		else if(datem == 5) {moncount = 31;}
		else if(datem == 6) {moncount = 30;}
		else if(datem == 7) {moncount = 31;}
		else if(datem == 8) {moncount = 31;}
		else if(datem == 9) {moncount = 30;}
		else if(datem == 10) {moncount = 31;}
		else if(datem == 11) {moncount = 30;}
		else if(datem == 12) {moncount = 31;}
		
		*/
		
		
		if(nowYear <= startYear) {
			if(nowMonth <= startMonth) {
				if(nowDay < startDay) {
					tc.sendMessage("���� �ȵſ�").queue();
					System.out.println("BOT: ���� �ȵſ�");
			
					return;
				}
			}	
		}

		if(startYear == 0) {
			if(startMonth >= nowMonth) {
				startYear = nowYear - 1;
			}
			else if(startMonth == nowMonth && startDay > nowDay)
				startYear = nowYear - 1;
		}
		
		
		int moncount = 0;
		
		while(run) {
			if(startYear == nowYear && startMonth == nowMonth && startDay == nowDay) {
				run = false;
				break;
			}
			
			dayCount = dayCount + 1;
			startDay = startDay + 1;
			
			if(startMonth == 1 ) {moncount = 31;}
			else if(startMonth == 2 ) {
				if(startYear%4 == 0) {moncount = 29;}
				else {startMonth = 28;}
			}
			else if(startMonth == 3) {moncount = 31;}
			else if(startMonth == 4) {moncount = 30;}
			else if(startMonth == 5) {moncount = 31;}
			else if(startMonth == 6) {moncount = 30;}
			else if(startMonth == 7) {moncount = 31;}
			else if(startMonth == 8) {moncount = 31;}
			else if(startMonth == 9) {moncount = 30;}
			else if(startMonth == 10) {moncount = 31;}
			else if(startMonth == 11) {moncount = 30;}
			else if(startMonth == 12) {moncount = 31;}
			
			if(startDay > moncount) {
				startMonth = startMonth + 1;
				startDay = startDay - moncount;
				
				if(startMonth > 12) {
					startMonth = startMonth - 12;
					startYear = startYear + 1;
				}
			}

		}
			
		String y;
		if(startYear == nowYear) {y = "";}
		else {y = String.valueOf(yearInt) + "�� ";}
			
		if(yearInt > 0) {
			tc.sendMessage("**" + yearInt + "�� " + mon + "�� " + day + "���� " + dayCount + "�� ��** �̾����").queue();
			System.out.println("BOT: **" + yearInt + "�� " + mon + "�� " + day + "���� " + dayCount + "�� ��** �̾����");

		}
		else {
			tc.sendMessage("**" + y + mon + "�� " + day + "���� " + dayCount + "�� ��** �̾����").queue();
			System.out.println("BOT: **" + y + mon + "�� " + day + "���� " + dayCount + "�� ��** �̾����");
		}
	}

	
	
}
