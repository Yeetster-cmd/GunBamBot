package main;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Scanner;


public class Administrator extends BotMusicListener {

	public static void admin(TextChannel tc, Message msg, Guild guild, User user, MessageReceivedEvent event) {

		
		Scanner scanner = new Scanner(System.in);
	    String str = scanner.nextLine();
	    tc = general;
    	
	    if(str.equalsIgnoreCase("/logout")) {
	    	/*
	    	tc.sendMessage("/logout").queue();
        	tc.sendMessage("```������ �α׾ƿ�```").queue();
        	
        	MessageHistory mh = new MessageHistory(tc);
            List<Message> msgs = mh.retrievePast(2).complete();
            tc.deleteMessages(msgs).complete();
        	
            tc.sendMessage("```���� �ٽ� ����մϴ�```").queue();
            */
        	
        	System.out.println("�α׾ƿ��մϴ�");
        	BotMusicListener.administrator = 0;
        	
        }
	    
	    else {
	    	tc.sendMessage(str).queue();
	    	System.out.println("BOT: " + str);
	    }

	}

}
