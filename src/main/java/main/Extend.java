package main;

import java.util.Random;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Extend extends BotListener{

public static void plusCommunication(TextChannel tc, User user, Message msg, Guild guild) {
		
	
	System.out.println("");
	System.out.println(guild + "\n" + user.toString() + ": " + msg.getContentRaw());
	
		
        if(msg.getContentRaw().contains("�ȳ�") || msg.getContentRaw().contains("�ݰ���")) {
			
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("�ݰ�����").queue();
				System.out.println("BOT: �ݰ�����");
			}
			
			else if(i == 1) {
				tc.sendMessage("����!").queue();
				System.out.println("BOT: ����!");
			}
			
			else if(i == 2) {
				tc.sendMessage("����").queue();
				System.out.println("BOT: ����");
			}
			
			else {
				tc.sendMessage("�ȳ�!").queue();
				System.out.println("BOT: �ȳ�!");
			}

		}

        else if(msg.getContentRaw().contains("����")||msg.getContentRaw().contains("��") || msg.getContentRaw().contains("����") || msg.getContentRaw().contains("�ƴϾ�")) {
		
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("��").queue();
				System.out.println("BOT: ��");
			}
			
			else if(i == 1) {
				tc.sendMessage("ĩ").queue();
				System.out.println("BOT: ĩ");
			}
			
			else if(i == 2) {
				tc.sendMessage("��..").queue();
				System.out.println("BOT: ��..");
			}
			
			else {
				tc.sendMessage("����").queue();
				System.out.println("BOT: ����");
			}
			
		
		}
		
		else if(msg.getContentRaw().contains("�ɽ���") || msg.getContentRaw().contains("����") || msg.getContentRaw().contains("���")) {
			
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("�÷���").queue();
				System.out.println("BOT: �÷���");
			}
			
			else if(i == 1) {
				tc.sendMessage("ȥ�ڳ�ƿ�").queue();
				System.out.println("BOT: ȥ�ڳ�ƿ�");
			}
			
			else if(i == 2) {
				tc.sendMessage("��� ��ƿ�").queue();
				System.out.println("BOT: ��� ��ƿ�");
			}
			
			else {
				tc.sendMessage("��..").queue();
				System.out.println("BOT: ��..");
			}
		
		}

		else if(msg.getContentRaw().contains("����")) {
			
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("�ڰ��־").queue();
				System.out.println("BOT: �ڰ��־");
			}
			
			else if(i == 1) {
				tc.sendMessage("�ڴ���").queue();
				System.out.println("BOT: �ڴ���");
			}
			
			else if(i == 2) {
				tc.sendMessage("�忡").queue();
				System.out.println("BOT: �忡");
			}
			
			else {
				tc.sendMessage("���ſ�").queue();
				System.out.println("BOT: ���ſ�");
			}
		
		}
        
		else if(msg.getContentRaw().contains("�߰�")||msg.getContentRaw().contains("�ٹ�")||msg.getContentRaw().contains("����")) {
			
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("�ٹ�").queue();
				System.out.println("BOT: �ٹ�");
			}
			
			else if(i == 1) {
				tc.sendMessage("�÷�").queue();
				System.out.println("BOT: �÷�");
			}
			
			else if(i == 2) {
				tc.sendMessage("�Ǻ���").queue();
				System.out.println("BOT: �Ǻ���");
			}
			
			else {
				tc.sendMessage("�ȴ�").queue();
				System.out.println("BOT: �ȴ�");
			}
		
		}
		
		else if(msg.getContentRaw().contains("����")||msg.getContentRaw().contains("���ڳٳ�")||msg.getContentRaw().contains("���ڳ���")) {
			
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("Zzz").queue();
				System.out.println("BOT: Zzz");
			}
			
			else if(i == 1) {
				tc.sendMessage("������").queue();
				System.out.println("BOT: ������");
			}
			
			else if(i == 2) {
				tc.sendMessage("�ڿ��ڿ�").queue();
				System.out.println("BOT: �ڿ��ڿ�");
			}
			
			else {
				tc.sendMessage("�ڱ�÷�").queue();
				System.out.println("BOT: �ڱ�÷�");
			}
		
		}
		
		else if(msg.getContentRaw().contains("����")||msg.getContentRaw().contains("���")) {
			
			Random r = new Random();
			int i = r.nextInt(3);
			if(i == 0) {
				tc.sendMessage("���⼭ �׷�������").queue();
	        	System.out.println("BOT: ���⼭ �׷�������");
			}
			
			else if(i == 1) {
				tc.sendMessage("�÷���").queue();
	        	System.out.println("BOT: �÷���");
			}
			
			else if(i == 2) {
				tc.sendMessage("�Ⱦ�").queue();
				System.out.println("BOT: �Ⱦ�");
			}
			
			else {
				tc.sendMessage("��������").queue();
				System.out.println("BOT: ��������");
			}
			
		}
        
		else if(msg.getContentRaw().contains("����")){
        	System.out.println("");
        	System.out.println(user.toString() + ": " + msg.getContentRaw());
        	
        	Random r = new Random();
        	int i = r.nextInt(3);
        	if(i == 0) {
        		tc.sendMessage("..?").queue();
        		System.out.println("BOT: ..?");
        	}
        	
        	else if(i == 1) {
        		tc.sendMessage("!!").queue();
        		System.out.println("BOT: !!");
        	}
        	
        	else if(i == 2) {
        		tc.sendMessage("��!").queue();
        		System.out.println("BOT: ��!");
        	}
        	
        	else {
        		tc.sendMessage("�ȵž�").queue();
        		System.out.println("BOT: �ȵž�");
        		
        	}

        }
 
        
		else if(msg.getContentRaw().contains("����")||msg.getContentRaw().contains("�� ��")){
        	

        	String minus = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	if(isNumber(minus) == true) {
        		int minusint = Integer.parseInt(minus);
        		
        		Command.dateCountMinus(minusint, tc);
        		
        		
        	}
        	
        	else {
        		Command.pardon(tc);
        	}
        	
        }
        
		else if(msg.getContentRaw().contains("�ϵ�")||msg.getContentRaw().contains("�� ��")||msg.getContentRaw().contains("����")||msg.getContentRaw().contains("�� ��")){
        	

        	String plus = msg.getContentRaw().replaceAll("[^0-9]", "");
        	
        	if(isNumber(plus) == true) {
        		int plusint = Integer.parseInt(plus);
        		
        		Command.dateCount(plusint, tc);
        		
        	}
        	
        	else {
        		Command.pardon(tc);
        	}
        	
        }
        
		else if(msg.getContentRaw().contains("���� �ޱ���")||msg.getContentRaw().contains("�����ޱ���")){
        	Command.nextMonth(tc, msg);
        }
        
		else if((msg.getContentRaw().contains("�ϱ���")||msg.getContentRaw().contains("�� ����"))&&(msg.getContentRaw().contains("��")||msg.getContentRaw().contains("��ĥ")||msg.getContentRaw().contains("����"))){
        	
			Command.untilDay(tc, msg);
		}
        
		else if(msg.getContentRaw().contains("����")&&(msg.getContentRaw().contains("����")||msg.getContentRaw().contains("��ĥ")||msg.getContentRaw().contains("����"))&&(msg.getContentRaw().contains("��")||msg.getContentRaw().contains("��")||msg.getContentRaw().contains("��"))){
        	
			Command.untilDay(tc, msg);
		}
    }
}
