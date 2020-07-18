package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import functions.*;
import main.BotMusicListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MusicController extends CustomFunctions
{
	final static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	
	private final static Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
	
	public static int isInTotal = 0;
	
	public static String botGuildsId = "", adminGuildsId = "";

	public static Message msg;
	public static HashMap<Guild, TextChannel> alertsList = new HashMap<Guild, TextChannel>();
	public static List<Guild> listg;
	public static List<Message> settingMessage;
	
	public static int gunBamStateRun = 0;
	
	public static Timer gunBamStateTimer = new Timer();
	
	public static TextChannel leaveTc;
	public static Message leaveMsg;
	public static MessageReceivedEvent leaveEvent;

	
	public static void go()    // Initialize everything when bot is started
	{
		playerManager.setPlayerCleanupThreshold(1800000);
		playerManager.setItemLoaderThreadPoolSize(8);
		
		playerManager.setTrackStuckThreshold(10000);
		
		playerManager.registerSourceManager(new YoutubeAudioSourceManager());
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
		
	}
	
	private static synchronized GuildMusicManager getGuildAudioPlayer(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event)    // Gets the music manager for each Guild
	{
		long guildId = Long.parseLong(guild.getId());
		
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null)
		{
			musicManager = new GuildMusicManager(playerManager, guild, tc, msg, event);
			musicManagers.put(guildId, musicManager);
	
		}

		try {
			
			guild.getAudioManager().setSendingHandler((AudioSendHandler) musicManager.getSendHandler());
			
		}
		catch(Exception e) {
			if(musicManager.scheduler.isIn == 1||musicManager.scheduler.isIn == 2) {
				isInTotal = isInTotal - 1;

			}
			
			String message = BotMusicListener.voiceTcMessage.get(guild);
			try {
				BotMusicListener.voiceTc.deleteMessageById(message).complete();
			}
			catch(Exception f) {}
			
			musicManagers.remove(guildId);
			alertsList.remove(guild);
			BotMusicListener.errortc.sendMessage(":no_entry_sign: (����ȭ ����) **" + e.getMessage() + "**" + staticCause(e)).queue();
		}
		return musicManager;
	}
	
	public static void playStandBy(TextChannel channel, Message msg, MessageReceivedEvent event, String input) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		musicManager.scheduler.playStandByInt = 1;
		
		if(input.startsWith("https://")) {
			musicManager.scheduler.playStandBy = input;
    	}
    	else { 
			musicManager.scheduler.playStandBy = input;
			
    	}
	}
	public static void loadAndPlay(final TextChannel channel, Message msg, Message res, String trackUr, MessageReceivedEvent event, String lan, int menu)    // Plays the song
	{
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);

		boolean isPaused = musicManager.player.isPaused();
		
		Member member = event.getMember();
		
		musicManager.scheduler.playStandByInt = 0;
		
		String languageAdd = ":hourglass: `" + trackUr + "` �ε� ��...";
		
		musicManager.scheduler.setLanguageStop = "kor";
		if(lan.equals("eng")) {
			languageAdd = ":hourglass: Loading `" + trackUr + "`.";
			musicManager.scheduler.setLanguageStop = "eng";
		}
		
		String stat = languageAdd;
		
		VoiceChannel myChannel = member.getVoiceState().getChannel();
		VoiceChannel botChannel = event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
		
		if(menu == 0) {
			if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
				String languageJoin = "**" + botChannel.getName() + "**�� �� �� �õ��ϼ���";
				
				if(lan.equals("eng")) {
					languageJoin = "Join **" + botChannel.getName() + "** and try again.";
				}
				
				channel.sendMessage(languageJoin).queue();
				playStandBy(channel, msg, event, trackUr);
				
				log(channel, event, "BOT: " + languageJoin);
				
				return;
			}
			
			else if(myChannel == null) {
				String languageJoin = "���� ����ä�ο� ������";
				
				if(lan.equals("eng")) {
					languageJoin = "Join the voice channel first.";
				}
				
				channel.sendMessage(languageJoin).queue();
	    		System.out.println("BOT: " + languageJoin);
	    		
	    		log(channel, event, "BOT: " + languageJoin);
	    		
	    		if(trackUr.length() > 1) {
	    			MusicController.playStandBy(channel, msg, event, trackUr);
	    		}
	    		return;
			}
			
		}

		if(trackUr.startsWith("https://")) {
			languageAdd = ":hourglass: �ε� ��..."; 
			
			if(lan.equals("eng")) {
				languageAdd = ":hourglass: Loading...";
			}
    		stat = languageAdd;
    	}
		
		else {
			if(staticIsNumber(trackUr)) {
				if(musicManager.scheduler.search == 1) {
					
					int nu = Integer.parseInt(trackUr);
					if(nu>0 && nu<=7) {
						languageAdd = "�߰� ��...";
							
						if(lan.equals("eng")) {
							languageAdd = "Adding...";
						}
							
						stat = languageAdd;
					}
					
				}
			}
			
			trackUr = "ytsearch:" + trackUr;
		}
		
		String trackUrl = trackUr;
		int men = menu;
		
		if(res != null) {
			res.delete().queue();
		}
		
		channel.sendMessage(stat).queue(response -> {
	
		try {
			if(!staticIsNumber(trackUrl.substring(BotMusicListener.queryCount))) {
				musicManager.scheduler.search = 0;
			}
			
			if(musicManager.scheduler.search == 1) {
				
				int a = Integer.parseInt(trackUrl.substring(BotMusicListener.queryCount)) - 1;
				
				if(a > 6) {a = 6;}
				else if(a < 0) {a = 0;}
				
				String duration = " ``(" + secTo((int)musicManager.scheduler.listSearch.get(a).getDuration()) + ")``";
				if(musicManager.scheduler.listSearch.get(a).getInfo().isStream == true) {
					duration = " ``(�����)``";
					if(lan.equals("eng"))
						duration = " ``(LIVE)``";
				}
				
				if (isPaused) {
					String language = "**" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "** �� �߰��߾�� (�Ͻ�������)" + duration;
					
					if(lan.equals("eng")) {
						language = "Added **" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "**. (Paused)" + duration;
					}
					
					String reply = "BOT: " + language + " `(�� " + (int)(musicManager.scheduler.queue.size() + 1) + "��)`";
					response.editMessage(language).queue();
					System.out.println(reply);
					log(channel, event, reply);
				}
				else {
					String language = "**" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "** �� �߰��߾��" + duration;
					
					if(lan.equals("eng")) {
						language = "Added **" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "**." + duration;
					}
					
					String reply = "BOT: " + language + " `(�� " + (int)(musicManager.scheduler.queue.size() + 1) + "��)`";
					response.editMessage(language).queue();
					System.out.println(reply);
					log(channel, event, reply);
				}
			

				play(channel.getGuild(), msg, member, musicManager, musicManager.scheduler.listSearch.get(a), channel, event, 1, 1);
				
				musicManager.scheduler.menu = 1;
				musicManager.scheduler.menuStr = musicManager.scheduler.listSearch.get(a).getInfo().uri;
				return;
			}
			
		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler()
		{
			@Override 
			public void trackLoaded(AudioTrack track) {
				
				String duration = " ``(" + secTo((int)track.getDuration()) + ")``";
				
				if(track.getInfo().isStream == true) {
					duration = " ``(�����)``";
					if(lan.equals("eng"))
						duration = " ``(LIVE)``";
				}
				
					if (isPaused) {
						String language = "**" + realTitle(track.getInfo().title) + "** �� �߰��߾�� (�Ͻ�������)" + duration;
						
						if(lan.equals("eng")) {
							language = "Added **" + realTitle(track.getInfo().title) + "**. (Paused)" + duration;
						}
						
						String reply = "BOT: " + language + " `(�� " + (int)(musicManager.scheduler.queue.size() + 1) + "��)`";
						response.editMessage(language).queue();
						System.out.println(reply);
						log(channel, event, reply);
					}
					else {
						String language = "**" + realTitle(track.getInfo().title) + "** �� �߰��߾��" + duration;
						
						if(lan.equals("eng")) {
							language = "Added **" + realTitle(track.getInfo().title) + "**." + duration;
						}
						
						String reply = "BOT: " + language+ " `(�� " + (int)(musicManager.scheduler.queue.size() + 1) + "��)`";
						response.editMessage(language).queue();
						System.out.println(reply);
						log(channel, event, reply);
					}
				

				play(channel.getGuild(), msg, member, musicManager, track, channel, event, 1, 1);
				
				if(men == 1) {
					musicManager.scheduler.menu = 0;
				}
				else {
					musicManager.scheduler.menu = 1;
					musicManager.scheduler.menuStr = track.getInfo().uri;
				}
			}

			@Override 
			public void playlistLoaded(AudioPlaylist playlist)  {
				// Only add the first song if it was searched for
				
				GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
				boolean isPaused = musicManager.player.isPaused();
				
				if (playlist.isSearchResult())
				{	
	
						String duration = " ``(" + secTo((int)playlist.getTracks().get(0).getDuration()) + ")``";
						
						if(playlist.getTracks().get(0).getInfo().isStream == true) {
							duration = " ``(�����)``";
							if(lan.equals("eng"))
								duration = " ``(LIVE)``";
						}
							
						if (isPaused) {
							String language = "**" + realTitle(playlist.getTracks().get(0).getInfo().title) + "** �� �߰��߾�� (�Ͻ�������)" + duration;
							
							if(lan.equals("eng")) {
								language = "Added **" + realTitle(playlist.getTracks().get(0).getInfo().title) + "**. (Paused)" + duration;
							}
							
							String reply = "BOT: " + language + " `(�� " + (int)(musicManager.scheduler.queue.size() + 1) + "��)`";
							response.editMessage(language).queue();    //Add the song to the queue
							System.out.println(reply);
							log(channel, event, reply);
						}
						else {
							String language = "**" + realTitle(playlist.getTracks().get(0).getInfo().title) + "** �� �߰��߾��" + duration;
							
							if(lan.equals("eng")) {
								language = "Added **" + realTitle(playlist.getTracks().get(0).getInfo().title) + "**." + duration;
							}
							
							String reply = "BOT: " + language + " `(�� " + (int)(musicManager.scheduler.queue.size() + 1) + "��)`";
							response.editMessage(language).queue();    //Add the song to the queue
							System.out.println(reply);
							log(channel, event, reply);
						}

						play(channel.getGuild(), msg, member, musicManager, playlist.getTracks().get(0), channel, event, 1, 1);
						musicManager.scheduler.menu = 1;
						musicManager.scheduler.menuStr = playlist.getTracks().get(0).getInfo().uri;
						return;
				}

				if (isPaused) {
					String language = "**" + playlist.getName() + "**�� **" + playlist.getTracks().size() + "��** �׸��� �߰��߾�� (�Ͻ�������)";
					
					if(lan.equals("eng")) {
						if(playlist.getTracks().size() <= 1) language = "Added **" + playlist.getTracks().size() + "item** of **" + playlist.getName() + "**. (Paused)";
						else language = "Added **" + playlist.getTracks().size() + "items** of **" + playlist.getName() + "**. (Paused)";
					}
					
					String reply = "BOT: " + language + " `(�� " + (int)(musicManager.scheduler.queue.size() + playlist.getTracks().size()) + "��)`";
					response.editMessage(language).queue();
					System.out.println(reply);
					log(channel, event, reply);
					
					
					musicManager.scheduler.menu = 4;
					musicManager.scheduler.recentAddPlayListCount = playlist.getTracks().size();
				}
				
				else {
					String language = "**" + playlist.getName() + "**�� **" + playlist.getTracks().size() + "��** �׸��� �߰��߾��";
					
					if(lan.equals("eng")) {
						if(playlist.getTracks().size() <= 1) language = "Added **" + playlist.getTracks().size() + "item** of **" + playlist.getName() + "**.";
						else language = "Added **" + playlist.getTracks().size() + "items** of **" + playlist.getName() + "**.";
					}
					
					String reply = "BOT: " + language + " `(�� " + (int)(musicManager.scheduler.queue.size() + playlist.getTracks().size()) + "��)`";
					response.editMessage(language).queue();
					System.out.println(reply);
					log(channel, event, reply);

					musicManager.scheduler.menu = 4;
					musicManager.scheduler.recentAddPlayListCount = playlist.getTracks().size();
				}
				
				/*
				for (AudioTrack track : playlist.getTracks())        // Add all the tracks
				{
					play(channel.getGuild(), msg, member, musicManager, track, channel, event, 1);
				}
				*/
				int ended = 0;
				for(int i = 0; i<playlist.getTracks().size(); i++) {
					if(i == playlist.getTracks().size()-1) ended = 1;
					play(channel.getGuild(), msg, member, musicManager, playlist.getTracks().get(i), channel, event, 1, ended);
				}
			}

			@Override 
			public void noMatches()
			{
				String ur = "";
				if(trackUrl.startsWith("https://")) ur = trackUrl;
				else ur = trackUrl.substring(BotMusicListener.queryCount);
				String language = "�ٽ� �õ��ϼ���\n**" + ur + "**";;
				
				if(lan.equals("eng")) {
					language = "Try again.\n**" + ur + "**";;
				}
				String reply = "BOT: " + language;
				
				if(musicManager.scheduler.tryAgain != 0)
					response.editMessage(language).queue();
				System.out.println(reply);
				log(channel, event, reply);
				
				MusicController.playStandBy(channel, msg, event, ur);
				
				if(musicManager.scheduler.tryAgain == 0) {
					loadAndPlay(channel, null, response, ur, event, language, men);
					musicManager.scheduler.tryAgain = 1;
				}
			}

			@Override 
			public void loadFailed(FriendlyException exception)
			{
				
				String reply = ":no_entry_sign: **" + exception.getMessage() + "**" + staticCause(exception);
				try {
					response.editMessage(reply).complete();
				}
				catch(Exception e) {
					channel.sendMessage(reply).queue();
				}
				System.out.println(reply);
				log(channel, event, reply);
				
				if(exception.getMessage().contains("Connecting")) return;
				
				if(exception.getMessage().contains("failed")) {
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + "> ����� �ȵ˴ϴ�").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
				}
			}
		});
		}
		catch(Exception e) {
			
			String reply = ":no_entry_sign: (loadItemOrdered) **" + e.getMessage() + "**" + staticCause(e);
			channel.sendMessage(reply).queue();
			log(channel, event, reply);
		}
		});
		
		
	}
	
	public static void loadAndPlayRandom(final TextChannel channel, Message msg, String trackUr, MessageReceivedEvent event, String eng)    // Plays the song
	{
		String stat = ":hourglass: �ε� ��...";
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		Member member = event.getMember();

		VoiceChannel myChannel = member.getVoiceState().getChannel();
		VoiceChannel botChannel = event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
		
		if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
			String language = "**" + botChannel.getName() + "**�� �� �� �õ��ϼ���";
			if(eng.equals("eng")) 
				language = "Join **" + botChannel.getName() + "** and try again.";
			
			channel.sendMessage(language).queue();
			playStandBy(channel, msg, event, trackUr);
			
			log(channel, event, "BOT: " + language);
			return;
		}
		
		else if(myChannel == null) {
			String language = "���� ����ä�ο� ������";
			if(eng.equals("eng")) 
				language = "Join the voice channel first.";
			
			channel.sendMessage(language).queue();
    		System.out.println("BOT: " + language);
    		
    		log(channel, event, "BOT: " + language);
    		
    		if(trackUr.length() > 1) {
    			MusicController.playStandBy(channel, msg, event, trackUr);
    		}
    		return;
		}
		
		channel.sendMessage(stat).queue(response -> {
		
		playerManager.loadItemOrdered(musicManager, trackUr, new AudioLoadResultHandler()
		{
			@Override 
			public void trackLoaded(AudioTrack track) {
				String language = "**������ URL**�� �ƴϳ׿�";
				if(eng.equals("eng")) 
					language = "It is not **URL links to a playlist**.";
				
				
				response.editMessage(language).queue();
				
			}

			@Override 
			public void playlistLoaded(AudioPlaylist playlist)  {
				// Only add the first song if it was searched for
				
				GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
				boolean isPaused = musicManager.player.isPaused();
				
				if (isPaused) {
					String language = "**" + playlist.getName() + "**�� **" + playlist.getTracks().size() + "��** �׸��� **��������** �߰��߾�� (�Ͻ�������)";
					if(eng.equals("eng")) {
						if(playlist.getTracks().size() <= 1) language = "Added **" + playlist.getTracks().size() + "item** of **" + playlist.getName() + " by **random**. (Paused)";
						else language = "Added **" + playlist.getTracks().size() + "items** of **" + playlist.getName() + " by **random**. (Paused)";
					}
					
					String reply = "BOT: " + language;
					response.editMessage(language).queue();
					System.out.println(reply);
					log(channel, event, reply);
				}
				
				else {
					String language = "**" + playlist.getName() + "**�� **" + playlist.getTracks().size() + "��** �׸��� **��������** �߰��߾��";
					if(eng.equals("eng")) {
						if(playlist.getTracks().size() <= 1) language = "Added **" + playlist.getTracks().size() + "item** of **" + playlist.getName() + " by **random**.";
						else language = "Added **" + playlist.getTracks().size() + "items** of **" + playlist.getName() + " by **random**.";
					}
					
					String reply = "BOT: " + language;
					response.editMessage(language).queue();
					System.out.println(reply);
					log(channel, event, reply);
				}

				musicManager.scheduler.menu = 4;
				musicManager.scheduler.recentAddPlayListCount = playlist.getTracks().size();
				
				int ended = 0;
				Collections.shuffle(playlist.getTracks());
				for(int i = 0; i<playlist.getTracks().size(); i++) {
					if(i == playlist.getTracks().size()-1) ended = 1;
					play(channel.getGuild(), msg, member, musicManager, playlist.getTracks().get(i), channel, event, 1, ended);
				}
			}

			@Override 
			public void noMatches()
			{
				String language = "�ٽ� �õ��� ������";
				if(eng.equals("eng")) {
					language = "Try again.";
				}
				
				String reply = "BOT: " + language;
				
				response.editMessage(reply).queue();
				System.out.println();
				log(channel, event, "BOT: " + reply);

			}

			@Override 
			public void loadFailed(FriendlyException exception)
			{
				
				String reply = ":no_entry_sign: **" + exception.getMessage() + "**"  + staticCause(exception);
				try {
					response.editMessage(reply).complete();
				}
				catch(Exception e) {
					channel.sendMessage(reply).queue();
				}
				System.out.println(reply);
				log(channel, event, reply);
				
				if(exception.getMessage().contains("Connecting")) return;
				
				
				if(exception.getMessage().contains("failed")) {
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + "> ����� �ȵ˴ϴ�").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
				}
			}
		});
		});

		
	}
	
	public static void loadAndPlaySub(final TextChannel channel, Message msg, final String trackUrl, MessageReceivedEvent event, Message response, int i, int ended)    // Plays the song
	{
		final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		Member member = event.getMember();
		
		
		try {
		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler()
		{
			@Override 
			public void trackLoaded(AudioTrack track) {
				if(musicManager.scheduler.terminate == 0) {
					play(channel.getGuild(), msg, member, musicManager, track, channel, event, i, ended);
					musicManager.scheduler.loadCount = musicManager.scheduler.loadCount - 1;
					musicManager.scheduler.recentAddPlayListCount = musicManager.scheduler.recentAddPlayListCount + 1;
				}
			}

			@Override 
			public void playlistLoaded(AudioPlaylist playlist)  {
				if (playlist.isSearchResult())
				{
					if(response == null) {}
					else response.editMessage("�߸��� ���� ����Դϴ� (������ Ȯ�� �ʿ�)").queue();
					
					log(channel, event, "<@" + BotMusicListener.admin + "> �߸��� ���� ����Դϴ� (������ Ȯ�� �ʿ�)");
					//play(channel.getGuild(), msg, member, musicManager, playlist.getTracks().get(0), channel, event, i, 0);
				}
			}

			@Override public void noMatches()
			{
				String reply = "BOT: �ٽ� �õ��ϼ���";
				musicManager.scheduler.loadCount = musicManager.scheduler.loadCount - 1;
				musicManager.scheduler.link.remove(trackUrl);
				
				if(response == null) {}
				else {
					if(musicManager.scheduler.playAgainEdited == 0) {
						response.editMessage("�ٽ� �õ��ϼ���").queue();
						musicManager.scheduler.playAgainEdited = 1;
					}
				}
				
				System.out.println(reply);

				log(channel, event, reply);

			}

			@Override public void loadFailed(FriendlyException exception)
			{
				
				String reply = ":no_entry_sign: **" + exception.getMessage() + "**" + staticCause(exception);
				if(response == null) {}
				else response.editMessage(reply).queue();
				
				System.out.println(reply);
				log(channel, event, reply);
				
				if(exception.getMessage().contains("Connecting")) return;
				
				if(exception.getMessage().contains("failed")) {
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + "> ����� �ȵ˴ϴ�").queue();
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + ">").queue();
				}
				
				musicManager.scheduler.link.remove(trackUrl);
				musicManager.scheduler.loadCount = musicManager.scheduler.loadCount - 1;
			}
		});
		
		}
		catch(Exception e) {
			String reply = ":no_entry_sign: (loadItemOrdered) **" + e.getMessage() + "**" + staticCause(e);
			if(response == null) {}
			else response.editMessage(reply).queue();
			
			log(channel, event, reply);
		}

	}

	public static void searchAndPlay(final TextChannel channel, Message msg, final String trackUrl, MessageReceivedEvent event, Member member, String lan)    // Plays the song
	{	
		final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		VoiceChannel myChannel = member.getVoiceState().getChannel();
		VoiceChannel botChannel = event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
		
		
		if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
			
			String language = "**" + botChannel.getName() + "**�� �� �� �õ��ϼ���";
	
			if(lan.equals("eng")) {
				language = "Join **" + botChannel.getName() + "** and try again.";
			}
			
			channel.sendMessage(language).queue();
			playStandBy(channel, msg, event, trackUrl);
			
			log(channel, event, "BOT: " + language);
			return;
		}
		
		else if(myChannel == null) {
			
			String language = "���� ����ä�ο� ������";
			
			if(lan.equals("eng")) {
				language = "Join the voice channel first.";
			}
			
			channel.sendMessage(language).queue();
    		System.out.println("BOT: " + language);
    		
    		log(channel, event, "BOT: " + language);
    		
    		if(trackUrl.length() > 1) {
    			MusicController.playStandBy(channel, msg, event, trackUrl);
    		}
    		return;
		}
		
		if(musicManager.scheduler.search == 1) {
			String language = "�߰� ��...";
			musicManager.scheduler.setLanguageStop = "kor";
			if(lan.equals("eng")) {
				language = "Adding...";
				musicManager.scheduler.setLanguageStop = "eng";
			}
			channel.sendMessage(language).queue(response -> {
				
				boolean isPaused = musicManager.player.isPaused();
		
				int a = Integer.parseInt(trackUrl) - 1;
						
				if(a > 7) {a = 6;}
				else if(a < 0) {a = 0;}
				
				String duration = " ``(" + secTo((int)musicManager.scheduler.listSearch.get(a).getDuration()) + ")``";
				if(musicManager.scheduler.listSearch.get(a).getInfo().isStream == true) {
					duration = " ``(�����)``";
					if(lan.equals("eng"))
						duration = " ``(LIVE)``";
				}
					
						
				if (isPaused) {
					String languageAdd = "**" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "** �� �߰��߾�� (�Ͻ�������)" + duration;
					
					if(lan.equals("eng")) {
						languageAdd = "Added **" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "**. (Paused)" + duration;
					}
					
					String reply = "BOT: " + languageAdd;
					response.editMessage(languageAdd).queue();
					System.out.println(reply);
					log(channel, event, reply);
				}
				else {
					
					String languageAdd = "**" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "** �� �߰��߾��" + duration;
					
					if(lan.equals("eng")) {
						languageAdd = "Added **" + realTitle(musicManager.scheduler.listSearch.get(a).getInfo().title) + "**." + duration;
					}
					
					String reply = "BOT: " + languageAdd;
					response.editMessage(languageAdd).queue();
					System.out.println(reply);
					log(channel, event, reply);
				}
					
			
				play(channel.getGuild(), msg, member, musicManager, musicManager.scheduler.listSearch.get(a), channel, event, 1, 1);
						
				musicManager.scheduler.menu = 1;
				musicManager.scheduler.menuStr = musicManager.scheduler.listSearch.get(a).getInfo().uri;
			});
		}

	}

	public static void search(TextChannel channel, Message msg, String query, MessageReceivedEvent event, Message response, String lan)    // search the song
	{
		
		final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.playStandByInt = 0;
		
		playerManager.loadItemOrdered(musicManager, query, new AudioLoadResultHandler()
		{
			@Override 
			public void trackLoaded(AudioTrack track) {
				String language = "�ܾ�� �˻��ϼ���";
				if(lan.equals("eng")) language = "Please input query not URL.";
						
				String reply = "BOT: " + language;
				response.editMessage(language).queue();
				System.out.println(reply);
				log(channel, event, reply);
			}

			@Override 
			public void playlistLoaded(AudioPlaylist playlist)  {
				if (playlist.isSearchResult())
				{
					
					musicManager.scheduler.listSearch.clear();
					
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(Color.decode(BotMusicListener.colorDefault));
					
					String languageTitle = ":mag: '" + query.substring(BotMusicListener.queryCount) + "' ��Ʃ�� �˻� ���";
					if(lan.equals("eng")) languageTitle = ":mag: Result '" + query.substring(BotMusicListener.queryCount) + "' from Youtube";
					
					eb.setTitle(languageTitle, "https://www.youtube.com/results?search_query=" + query.substring(BotMusicListener.queryCount).replaceAll(" ", "+"));
					
					StringBuilder sb = new StringBuilder();
			        	
					for (int i = 0; i < 7 && i < playlist.getTracks().size(); i++) {

						AudioTrack track = playlist.getTracks().get(i);
						musicManager.scheduler.listSearch.add(track);
							
						String num = "";
							
						if(i == 0) num = ":one:";
						else if(i == 1) num = ":two:";
						else if(i == 2) num = ":three:";
						else if(i == 3) num = ":four:";
						else if(i == 4) num = ":five:";
						else if(i == 5) num = ":six:";
						else if(i == 6) num = ":seven:";
						
						String duration = " `[" + secTo((int)track.getDuration()) + "]`";
						if(track.getInfo().isStream == true) {
							duration = " `[�����]`";
							if(lan.equals("eng")) {
								duration = " `[LIVE]`";
							}
						}
							
							
						sb.append(num + duration + " **" + realTitle(track.getInfo().title) + "**\n");
					}
					
		
					eb.setDescription(sb);
			            
					response.editMessage(eb.build()).queue();
			        System.out.println("BOT: \n" + sb);
			        log(channel, event, "BOT: \n" + sb);
			      
	
					musicManager.scheduler.search = 1;
					musicManager.scheduler.menu = 7;
				}
			}

			@Override public void noMatches()
			{
				String language = "�ٽ� �õ��ϼ���\n`" + query.substring(BotMusicListener.queryCount) + "`";
				if(lan.equals("eng"))
					language = "Try again.\n`" + query.substring(BotMusicListener.queryCount) + "`";
				
				String reply = "BOT: " + language;
				
				if(musicManager.scheduler.tryAgain != 0) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(Color.decode(BotMusicListener.colorDefault));
					eb.setTitle(":x: " + language);
					
					response.editMessage(eb.build()).queue();	
				}
				
				System.out.println(reply);
				log(channel, event, reply);
				
				musicManager.scheduler.playStandByInt = 2;
				musicManager.scheduler.playStandBy = query;
				
				if(musicManager.scheduler.tryAgain == 0) {
					
					EmbedBuilder eb = new EmbedBuilder();
    				eb.setColor(Color.decode(BotMusicListener.colorDefault));
    				
    				String langu = ":mag: `" + query.substring(BotMusicListener.queryCount) + "` �˻� ��...";
            		if(language.equals("eng"))
            			langu = ":mag: Searching `" + query.substring(BotMusicListener.queryCount) + "`...";
            		
    				eb.setTitle(langu);
    				
    				response.editMessage(eb.build()).queue();
    				
					search(channel, msg, query, event, response, language);
					musicManager.scheduler.tryAgain = 1;
				}
				
			}

			@Override public void loadFailed(FriendlyException exception)
			{
				
			}
		});
	}
	
	
	private static void play(Guild guild, Message msg, Member member, GuildMusicManager musicManager, AudioTrack track, TextChannel channel, MessageReceivedEvent event, int i, int ended)
	{
		member = event.getMember();
		if(musicManager.scheduler.isIn == 0) {
			musicManager.scheduler.isIn = 1;
			musicManager.scheduler.enteredTime = System.currentTimeMillis();
			
			connectToMusicVoiceChannel(channel, msg, member, guild.getAudioManager(), event);
			guild.getAudioManager().setAutoReconnect(false);
		
		}
	
		musicManager.scheduler.queue(channel, event, track, i, 0);
	}
	
	public static void playAgain(TextChannel channel, Message msg, Member member, MessageReceivedEvent event, String id, int menu, String lan)
	{
		Guild guild = channel.getGuild();
		member = event.getMember();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);

		if(menu == 0) {
			if(musicManager.scheduler.isIn == 0) {
			
				musicManager.scheduler.enteredTime = System.currentTimeMillis();
				if(menu == 0) {
					musicManager.scheduler.queueAgain(channel, msg, lan);
					musicManager.scheduler.playAgainEdited = 0;
				}
				
				else {
					musicManager.scheduler.queueAgainPersonal(channel, msg, id, lan);
				}
			}
			else {
				String reply = "BOT: ���� ����ä�ο� �־��";
				channel.sendMessage("���� ����ä�ο� �־��").queue();
				System.out.println(reply);
				log(channel, event, reply);
				
			}
		}
		
		else {
			musicManager.scheduler.queueAgainPersonal(channel, msg, id, lan);
		}
	}

	public static void connectToMusicVoiceChannel(TextChannel channel, Message msg, Member member, AudioManager audioManager, MessageReceivedEvent event) {  // Connects to the voice channel
		
		try {
			VoiceChannel myChannel = member.getVoiceState().getChannel();    //Connect the the voice channel the user is in	
			if (myChannel != null) {
					
				audioManager.openAudioConnection(myChannel);
					
					
				String reply = "BOT: ����ä�ο� ������ �ʾ� " + myChannel.getId() + " ``(" + myChannel.getName() + ")`` �� ���Ծ��";
				System.out.println(reply);
				log(channel, event, reply);
					
				leaveTc = channel;
				leaveMsg = msg;
				leaveEvent = event;
			}
		
			
			isInTotal = isInTotal + 1;
			
			Runnable write1 = () -> {
				
				File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(String.valueOf(isInTotal));
					fw.close();
				}
				catch (Exception e) {
					e.printStackTrace();
					channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
				}
			};
			
			Runnable write2 = () -> {
				File file = new File(BotMusicListener.directoryDefault + "guild/usingGuilds.txt");
				try {
					FileWriter fw = new FileWriter(file, true);
					if(file.length() <= 1)
						fw.append(channel.getGuild().getId() + "/" + channel.getId());
					else
						fw.append("\n" + channel.getGuild().getId() + "/" + channel.getId());
					
					fw.close();
				}
				catch (Exception e) {
					e.printStackTrace();
					channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
					log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
				}
			};
			
			Thread t1 = new Thread(write1);
			Thread t2 = new Thread(write2);
			t1.start();
			t2.start();
				
			editguilds(channel, msg, event);
		}
		catch(Exception e) {
			channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
			log(channel, event, ":no_entry_sign: **" + e.getMessage() + "** "+ staticCause(e));
		}

	}
	
	public static void queryNull(TextChannel channel, Message msg, MessageReceivedEvent event, Member member, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(musicManager.scheduler.playStandByInt == 1) { 
			if(musicManager.scheduler.playStandBy.startsWith("https://")) {
				MusicController.loadAndPlay(channel, msg, null, musicManager.scheduler.playStandBy, event, lan, 0);
			}
			else {
				MusicController.loadAndPlay(channel, msg, null, musicManager.scheduler.playStandBy, event, lan, 0);
				
			}
			
			MusicController.repeat(channel, msg, event);
    		
    		return;
		}
		
		boolean isPaused = musicManager.player.isPaused();
		
		if(isPaused) {
			resume(channel, event, member, lan, 1);
		}
		else {
			String language = "�ùٸ� URL�� �Է��ϼ���";
			
			if(lan.equals("eng")) {
				language = "Please input a valid URL.";
			}
			
			String reply = "BOT: " + language;
			channel.sendMessage(language).queue();
	        System.out.println(reply);
	        
			log(channel, event, reply);
		}

	}
	
	public static void queryNullSearch(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(musicManager.scheduler.playStandByInt == 2) { 
			
			String query = musicManager.scheduler.playStandBy;
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.decode(BotMusicListener.colorDefault));
			
			String langu = ":mag: `" + query.substring(BotMusicListener.queryCount) + "` �˻� ��...";
    		if(lan.equals("eng"))
    			langu = ":mag: Searching `" + query.substring(BotMusicListener.queryCount) + "`...";
    		
			eb.setTitle(langu);
			
			String langua = lan;
			
        	channel.sendMessage(eb.build()).queue(response -> {
	        	
        		MusicController.search(channel, msg, query, event, response, langua);
	        });
        	
			MusicController.repeat(channel, msg, event);
    		
    		return;
		}
		
		String langu = "�ùٸ� �ܾ �Է����ּ���";
		if(lan.equals("eng"))
			langu = "Please input a valid query.";
			
	    channel.sendMessage(langu).queue();
	    System.out.println("BOT: " + langu);
	        
		log(channel, event, "BOT: " + langu);
	}
	
	public static void skipTrack(TextChannel channel, Message msg, MessageReceivedEvent event, int skipto, String lan) { // Skip the current track and play the next one
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.nextTrack(channel, msg, event, 1, skipto, lan);
	}
	
	public static void randomNextTrack(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) { // Skip the current track and play the next one
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(musicManager.scheduler.playStandByInt == 1) {
			if(musicManager.scheduler.playStandBy.startsWith("https://")) {
				MusicController.loadAndPlayRandom(channel, msg, musicManager.scheduler.playStandBy, event, lan);
			}
			else {
				String language = "������ URL�� �����ؿ�";
				if(lan.equals("eng"))
					language = "Only URL links to playlist possible.";
					
				channel.sendMessage(language).queue();
				log(channel, event, "BOT: " + language);
			}
			
			MusicController.repeat(channel, msg, event);
    		musicManager.scheduler.playStandByInt = 0;
	
		}
		else {
			if(musicManager.scheduler.queue.isEmpty()) {
				String language = "�ùٸ� URL�� �Է��ϼ���";
				if(lan.equals("eng"))
					language = "Please input a valid URL.";
				
				channel.sendMessage(language).queue();
				log(channel, event, "BOT: " + language);
				
				return;
			}
			
			Random r = new Random();
			int skipto = r.nextInt(musicManager.scheduler.queue.size() + 1);
			musicManager.scheduler.nextTrack(channel, msg, event, 1, skipto, lan);
		}
	}
	
	public static void now(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.nowInfo(channel, msg, event, lan);	
	}
	
	public static void savedlist(TextChannel channel, Message msg, MessageReceivedEvent event, int page, int fn, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(fn == 2) {
			int in = ((int)(musicManager.scheduler.current + 1) / 10) + 1;
    		page = in;
		}
		
		musicManager.scheduler.savedlist(channel, msg, page, lan);
	}
	
	public static void savePersonalPlaylist(TextChannel channel, Message msg, MessageReceivedEvent event, String id, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.savePersonalPlaylist(channel, msg, event, id, lan);
	}
	
	public static void stopPlaying(TextChannel channel, Message msg, MessageReceivedEvent event, int i, int save, String lan) { // Reset everything and disconnect
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		channel.getGuild().getAudioManager().setSelfMuted(false);
		
		if(musicManager.scheduler.isIn == 0) {
			String language = "�̹� �����־��";
			if(lan.equals("eng"))
				language = "Already left from the voice channel";
			
			try {
				channel.sendMessage(language).complete();
			}
			catch(Exception e) {}
			String reply = "BOT: " + language;
			System.out.println(reply);
			log(channel, event, reply);
		}
		
		else {
	
			musicManager.scheduler.terminate = 1;
			musicManager.scheduler.isPlay = 0;
			
			String language = ":clipboard: `ó�� ��...`";
			if(lan.equals("eng"))
				language = ":clipboard: `Processing...`";
	
			channel.sendMessage(language).queue(response -> {

				Runnable up = () -> {
					BotMusicListener.update(guild);
				};
				Thread t1 = new Thread(up);
				t1.start();

				if(save == 0) musicManager.scheduler.save = 0;
				
				musicManager.scheduler.clearQueue(channel, msg, response, guild, event, i, musicManager.scheduler.save, lan);
		
				isInTotal = isInTotal - 1;
		
				Runnable remove1 = () -> {
					File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
					try {
						FileWriter fw = new FileWriter(file);
						fw.write(String.valueOf(isInTotal));
						fw.close();
					}
					catch (Exception e) {
						e.printStackTrace();
						response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
						log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
					}
				};
				
				Runnable remove2 = () -> {
					
					StringBuilder sb = new StringBuilder();
					File file = new File(BotMusicListener.directoryDefault + "guild/usingGuilds.txt");
					
					try{   
			            FileReader filereader = new FileReader(file);
			            BufferedReader bufReader  =  new BufferedReader(filereader);
		
			            String line = "";
			            while((line = bufReader.readLine()) != null){
			            	if(line.contains(channel.getGuild().getId())) {}
			            	else {
			            		if(sb.length() == 0) sb.append(line);
			            		else sb.append(line + "\n");
			            		}
			            
			            }
			            //.readLine()�� ���� ���๮�ڸ� ���� �ʴ´�.            
			            bufReader.close();
			            
			            FileWriter fw = new FileWriter(file);
						fw.write(sb.toString());
						fw.close();
			        }
					catch(Exception e){
			            System.out.println(e);
			            response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
		   				log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
			        }
	
				};
					
				Thread rt1 = new Thread(remove1);
				rt1.start();
				Thread rt2 = new Thread(remove2);
				rt2.start();

				editguilds(channel, msg, event);
			});
			

			if (guild.getAudioManager().isConnected()) {
				guild.getAudioManager().closeAudioConnection();  
			}
			
			
			
			/*
			if(isInTotal == 0) {
				if(gunBamStateRun == 1) {
					gunBamStateTimer.cancel(); 
					gunBamStateTimer = null;
					gunBamStateRun = 0;		
				}
			}
			*/

		}

	}
	
	public static void last(TextChannel channel, Message msg, User user, MessageReceivedEvent event, int i, int last, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);

		musicManager.scheduler.last(channel, msg, event, user, i, last, lan);
         
	}

	public static void shuffle(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);

		musicManager.scheduler.shuffle(channel, msg, lan);
	}
	
	public static void cancel(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);

		musicManager.scheduler.cancel(channel, msg, event, lan);  
	}
	
	public static void clear(TextChannel channel, List<Message> msgs, Message msg, MessageReceivedEvent event, int co) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.clearMessage(channel, msgs, msg, co);
	}
	
	public static void nowplay(TextChannel channel, Message msg, MessageReceivedEvent event, int i, int fn, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		
		if(fn == 2) {
			int in = ((int)(musicManager.scheduler.current + 1) / 10) + 1;
			
			i = in;
		}
		
        musicManager.scheduler.nowplay(channel, msg, event, i, lan);  
	}
	
	public static void repeat(TextChannel channel, Message msg, MessageReceivedEvent event) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		
		musicManager.scheduler.tryAgain = 0;
        musicManager.scheduler.repeat(channel, msg, event);
	}
	
	public static void removeManyTrack(List<Integer> n, TextChannel channel, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
	
        musicManager.scheduler.removeMany(n, channel, event, lan); 
	}

	public static void removeTrack(String s, TextChannel channel, MessageReceivedEvent event, int val, String lan, int many) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		
        musicManager.scheduler.remove(s, channel, event, val, 1, lan);
	}
	
	public static void volume(TextChannel channel, int volume, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		musicManager.scheduler.setVolume(channel, event, volume, 1, lan);
	}
	
	public static void nowVolume(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
	
		musicManager.scheduler.nowVolume(channel, event, msg, lan);
	}
	
	public static void timer(TextChannel channel, Message msg, User user, int time, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
		
		if(musicManager.scheduler.isPlay == 0) {
			String reply = "������� �׸��� �־�� �ؿ�";
			channel.sendMessage("������� �׸��� �־�� �ؿ�").queue();
			System.out.println(reply);
			log(channel, event, reply);
			return;
		}
		
		musicManager.scheduler.setTimer(channel, msg, user, event, time, lan);
	}
	
	public static void nowTimer(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
	
		musicManager.scheduler.nowTimer(channel, msg, lan);
	}
	
	public static void timerCancel(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, channel, msg, event);
	
		musicManager.scheduler.timerCancel(channel, msg, lan);
	}
	
	public static void resume(TextChannel channel, MessageReceivedEvent event, Member member, String lan, int send) { // Resume music that was paused
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(send == 1) {
			if(musicManager.scheduler.isPlay == 1) {
				VoiceChannel myChannel = member.getVoiceState().getChannel();
				VoiceChannel botChannel = event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
				
				if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
					String languageJoin = "**" + botChannel.getName() + "**�� �� �� �õ��ϼ���";
					
					if(lan.equals("eng")) {
						languageJoin = "Join **" + botChannel.getName() + "** and try again.";
					}
					
					channel.sendMessage(languageJoin).queue();
					
					log(channel, event, "BOT: " + languageJoin);
					return;
				}
			}
			
			else {
				String languageJoin = "������� �׸��� �־�� �ؿ�";
				
				if(lan.equals("eng")) {
					languageJoin = "There is no item being played.";
				}
				
				channel.sendMessage(languageJoin).queue();
				
				log(channel, event, "BOT: " + languageJoin);
				return;
			}
		}
		
		boolean isPaused = musicManager.player.isPaused();

		if (isPaused) {
			musicManager.player.setPaused(false);
			channel.getGuild().getAudioManager().setSelfMuted(false);
			
			musicManager.scheduler.isIn = 1;
				
			if(send == 1) {
				String language = "�÷��̾ ������·� �����ؿ�";
	        	if(lan.equals("eng")) language = "It will play this player.";
	        		
				channel.sendMessage(language).queue();
					
				String reply = "BOT: " + language;
				System.out.println();
				log(channel, event, reply);
			}
				
			editguilds(channel, msg, event);
		}
		else {
			if(send == 1) {
				String language = "�̹� ������·� �����Ǿ��־��";
	        	if(lan.equals("eng")) language = "Already playing  in this server.";
	        		
				channel.sendMessage(language).queue();
				String reply = "BOT: " + language;
				System.out.println(reply);
				log(channel, event, reply);
			}
		}
	}
	
	public static void pause(TextChannel channel, MessageReceivedEvent event, Member member, String lan, int send) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		if(send == 1) {
			if(musicManager.scheduler.isPlay == 1) {
				VoiceChannel myChannel = member.getVoiceState().getChannel();
				VoiceChannel botChannel = event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel();
				
				if(musicManager.scheduler.isPlay == 1 && (myChannel != botChannel)) {
					String languageJoin = "**" + botChannel.getName() + "**�� �� �� �õ��ϼ���";
					
					if(lan.equals("eng")) {
						languageJoin = "Join **" + botChannel.getName() + "** and try again.";
					}
					
					channel.sendMessage(languageJoin).queue();
					
					log(channel, event, "BOT: " + languageJoin);
					
					return;
				}
			}
			else {
				String languageJoin = "������� �׸��� �־�� �ؿ�";
				
				if(lan.equals("eng")) {
					languageJoin = "There is no item being played.";
				}
				
				channel.sendMessage(languageJoin).queue();
				
				log(channel, event, "BOT: " + languageJoin);
				
				return;
			}
		}
		
		boolean isPaused = musicManager.player.isPaused();

		
		if(!isPaused) {
			musicManager.player.setPaused(true);
			channel.getGuild().getAudioManager().setSelfMuted(true);
			musicManager.scheduler.isIn = 2;
			
			if(send == 1) {
				String language = "�÷��̾ �Ͻ������߾��";
				if(lan.equals("eng")) language = "Paused this player.";
			
				channel.sendMessage(language).queue();
				String reply = "BOT: " + language;
				System.out.println(reply);	
				log(channel, event, reply);
			}
			
			editguilds(channel, msg, event);
		}
		else {
			String language = "�̹� �Ͻ������߾��";
			if(lan.equals("eng")) language = "Already paused this player.";
			
			channel.sendMessage(language).queue();
			String reply = "BOT: " + language;
			System.out.println(reply);
			log(channel, event, reply);
		}

	}
	
	//(manyPeople)
	public static void lock(TextChannel channel, MessageReceivedEvent event, int val) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		musicManager.scheduler.lock = val;
	}
	
	public static void save(TextChannel channel, MessageReceivedEvent event, int val) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		if(val == 2) {
			channel.sendMessage("saveDefault: " + musicManager.scheduler.save).queue();
        	System.out.println("BOT: saveDefault: " + musicManager.scheduler.save);

		}
		else {
			musicManager.scheduler.save = val;
		}
	}
	
	public static void alertTc(TextChannel channel, MessageReceivedEvent event) {
		Guild guild = event.getGuild();

		alertsList.put(guild, channel);

	}
	
	public static void guilds(TextChannel channel, Message msg, MessageReceivedEvent event) {
			
		Runnable r1 = () -> {
			try {
				BotMusicListener.adtc.deleteMessageById(adminGuildsId).complete();
			}
	    	catch(Exception e) {}
				
			adminGuildsId = msg.getId();
		};

		Runnable r2 = () -> {
			try {
				BotMusicListener.adtc.deleteMessageById(botGuildsId).complete();
			}
	    	catch(Exception e) {}
		};
		
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		t1.start();
		t2.start();
		
		BotMusicListener.adtc.sendMessage("�ε� ��...").queue(response -> {
			botGuildsId = response.getId();
			botGunBamState(channel, msg, event, response, 0);
		});
			
	}
	
	public static void editguilds(TextChannel channel, Message msg, MessageReceivedEvent event) {
		botGunBamState(channel, msg, event, null, 1);
	}
	
	public static void channel(TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.channel(channel, msg, lan);
	}
	
	public static void prepare(TextChannel channel, Message msg, MessageReceivedEvent event) {
		
		listg = new ArrayList<>();
		listg.clear();
		if(event == null) {}
		else listg.addAll(event.getJDA().getGuilds());
		
		Runnable load1 = () -> {
			File file2 = new File(BotMusicListener.directoryDefault + "newsPrefix.txt");
			
			try {
				FileReader filereader2 = new FileReader(file2);
		       
		        BufferedReader bufReader2 = new BufferedReader(filereader2);
		        String line2 = "";
		        while((line2 = bufReader2.readLine()) != null){
		        	 BotMusicListener.prefix = line2;
		        }
		               
		        bufReader2.close();
			}
			catch(Exception e) {
				channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
			}
		};

		Thread t1 = new Thread(load1);
		t1.start();
		
	}
	
	public static void memoSave(TextChannel channel, Message msg, MessageReceivedEvent event, Message response) {
		GuildMusicManager musicManager;
		
		StringBuilder s = new StringBuilder();
		s.append("**" + listg.size() + "��** �������� Ȱ�� ���̿���\n```css\n");
				
		for(int i = 0; i<listg.size(); i++) {
			s.append(listg.get(i).toString() + "\n");

		}

		s.append("```\n**" + (int)(alertsList.size() - 1) + "��** ä�ο� ������ �غ� �Ǿ����\n```css\n");

		Iterator<Guild> it = alertsList.keySet().iterator();
				
		while(it.hasNext()) {
			Guild g = it.next();
			TextChannel value = alertsList.get(g);
					
			if(g.toString().contains(BotMusicListener.base)) s.append(g.toString() + ": [" + value.toString() + "]\n");
			else s.append(g.toString() + ": " + value.toString() + "\n");
						
		}
				
			
		s.append("```\n**" + (int)(isInTotal) + "��** ����ä�ο� ������ �־��\n```css\n");
		Iterator<Guild> it2 = alertsList.keySet().iterator();
				
		while(it2.hasNext()) {
			Guild g = it2.next();
					
			musicManager = getGuildAudioPlayer(g, channel, msg, event);
					
			if(musicManager.scheduler.isIn == 1||musicManager.scheduler.isIn == 2) {
				s.append(g + ": " + musicManager.scheduler.isIn + " (" + ((long)(System.currentTimeMillis() - musicManager.scheduler.enteredTime)/1000)/3600 + " �ð�)\n");
			}
			
			else s.append(g + ": " + musicManager.scheduler.isIn + "\n");	
		}
		
		s.append("```");
				
				
		File file2 = new File(BotMusicListener.directoryDefault + "��Ȳ������.txt");
		try {
			FileWriter fw = new FileWriter(file2);
			fw.write(s.toString());
			fw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
		}
				
		if(response == null) {}
		else response.editMessage("**��Ȳ��.txt** �� �����߾��").queue();
		
	}
	
	public static void botGunBamState(TextChannel channel, Message msg, MessageReceivedEvent event, Message response, int val) {

		StringBuilder s = new StringBuilder();

		prepare(channel, msg, event);
		
		s.append("**" + listg.size() + "��** �������� Ȱ�� ���̿���\n");
		s.append("**" + (int)(isInTotal) + "��** ����ä�ο� ������ �־��\n");
		
		if(val == 0) {
			response.editMessage(s).queue();
			
			if(gunBamStateRun == 1) {
				gunBamStateTimer.cancel(); 
				gunBamStateTimer = null;
				gunBamStateRun = 0;		
			}
			
			gunBamStateTimer = new Timer();
			gunBamStateRun = 1;
			
			updateGuild(channel, msg, event);
		
		}
		else {
			Runnable edit = () -> {
				try {
					BotMusicListener.adtc.editMessageById(botGuildsId, s).complete();
					MusicController.memoSave(channel, msg, event, null);
				}
				catch(Exception e) {
					/*
					if(gunBamStateRun == 1) {
						gunBamStateRun = 0;
						gunBamStateTimer.cancel();
						gunBamStateTimer = null;
					}
					*/
					//BotMusicListener.adtc.sendMessage(":warning: **" + e.getMessage() + "**" + staticCause(e)).queue();
							
				}
				
			};
            Thread t1 = new Thread(edit);
            t1.start();

			if(isInTotal == 0) {
				if(BotMusicListener.allExitAlert == 1) {
					BotMusicListener.logtc.sendMessage("<@" + BotMusicListener.admin + "> ��� ������ ����ä���� �������").queue();
					BotMusicListener.allExitAlert = 0;
				}
				/*
				if(gunBamStateRun == 1) {
					gunBamStateRun = 0;
					gunBamStateTimer.cancel();
					gunBamStateTimer = null;
				}
				*/
				
			}
	
		}
		
	}
	
	public static void updateGuild(TextChannel channel, Message msg, MessageReceivedEvent event) {
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	editguilds(channel, msg, event);
            	
            	Runnable r = () -> {
            		memoSave(channel, msg, event, null);
            		fixGuilds(channel, msg, event, null);
            	};
            	Thread t1 = new Thread(r);
            	t1.start();
            }
				
        };
        
        gunBamStateTimer.scheduleAtFixedRate(task, 1800000, 1800000);
	}
	
	public static void fixGuilds(TextChannel channel, Message msg, MessageReceivedEvent event, Message response) {
		try {
			Iterator<Guild> it = alertsList.keySet().iterator();
			isInTotal = 0;
			GuildMusicManager musicManager;
			while(it.hasNext()) {
				Guild g = it.next();
				
				musicManager = getGuildAudioPlayer(g, channel, msg, event);
				
				AudioManager manager = g.getAudioManager();
				
				if(manager.isConnected()) {
					isInTotal = isInTotal + 1;
				}
				else {
					if(musicManager.scheduler.isIn != 0 || musicManager.scheduler.isPlay == 1) {
						if(channel.getGuild().getId().equals(BotMusicListener.born)) {
	            			MusicController.stopPlaying(channel, msg, event, 3, musicManager.scheduler.save, musicManager.scheduler.setLanguageStop);
	                    }
	            			
	                    else {
	                    	MusicController.stopPlaying(channel, msg, event, 3, 0, musicManager.scheduler.setLanguageStop);
	                    }
						
					}
				}
			}
			
			Runnable r = () -> {
				File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
				try {
				    FileWriter fw = new FileWriter(file);
				    fw.write(String.valueOf(isInTotal));
				    fw.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				    channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();  
				}
			};
			Thread t = new Thread(r);
			t.start();
	
			editguilds(channel, msg, event);
	
			if(response == null) {}
			else response.editMessage("�����߾��").queue();
		}
		catch(Exception e) {
		
			if(response == null) {channel.sendMessage("�ٽ� �õ��ϼ���").queue();}
			else response.editMessage("�ٽ� �õ��ϼ���").complete();
			
		}
	}
	
	public static void saveGuilds(TextChannel channel, MessageReceivedEvent event) {
		File file = new File(BotMusicListener.directoryDefault + "guild/guildListSave.txt");
		Iterator<Guild> it = alertsList.keySet().iterator();
		try {
			FileWriter fw = new FileWriter(file);
		      
		    for(int k = 0; k<alertsList.size(); k++) {
		    	Guild g = it.next();
				TextChannel value = alertsList.get(g);
				if(g.toString().contains(BotMusicListener.base)) {}
				else
					fw.write(g.toString() + ": " + value.toString() + "\n");
		    	 
		    }
		      
		    fw.close();

		} 
		catch (Exception e) {
		    e.printStackTrace();
		    channel.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
		}
		
		channel.sendMessage("����� �����߽��ϴ� (ũ��: " + alertsList.size() + ")").queue();
	}
	
	public static void disconnect(int code, DisconnectEvent event) {
		try {
			Iterator<Guild> it = alertsList.keySet().iterator();
			
			GuildMusicManager musicManager;
			
			if(event.getCloseCode().toString().contains("null")) {
				return;
			}
			
			while(it.hasNext()) {
				Guild g = it.next();
				musicManager = getGuildAudioPlayer(g, alertsList.get(g), msg, null);
	
				if(!event.getJDA().getStatus().toString().equals("CONNECTED")) {
					if(code == 1008||code == 1000) {
						if(musicManager.scheduler.waitingToReconnect == 1) {
							if(g.getId().equals(BotMusicListener.born)) {
								String language = ":globe_with_meridians: ��Ʈ��ũ�� **�����Ǿ����** (��� �����)";
				    			if(musicManager.scheduler.setLanguageStop.equals("eng"))
				    				language = ":globe_with_meridians: **Reconnected** to discord. (Saved)";
				    			
								String reply = "BOT: " + language;
								alertsList.get(g).sendMessage(language).queue();
								System.out.println(reply);
		
			                }
			        			
			                else {
			                	String language = ":globe_with_meridians: ��Ʈ��ũ�� **�����Ǿ����**";
				    			if(musicManager.scheduler.setLanguageStop.equals("eng"))
				    				language = ":globe_with_meridians: **Reconnected** to discord.";
				    			
								String reply = "BOT: " + language;
								alertsList.get(g).sendMessage(language).queue();
								System.out.println(reply);
			                }

						}
						
						musicManager.scheduler.waitingToReconnect = 0;
						
						Runnable up = () -> {
							BotMusicListener.update(g);
						};
						Thread t1 = new Thread(up);
						t1.start();
					}
					
				}
				
				else {
					if(musicManager.scheduler.isIn != 0) {
						alertsList.get(g).sendMessage(":page_with_curl: **" + event.getClientCloseFrame().getCloseReason() + "**").queue();
					}
				}
			}

		}
		catch(Exception e) {}
	}

	public static void alert(TextChannel channel, MessageReceivedEvent event) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
	
		musicManager.scheduler.alert(channel);
	}
	
	public static void ping(Guild guild, TextChannel channel, Message msg, MessageReceivedEvent event, String lan) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);

		musicManager.scheduler.ping(guild, channel, msg, event, lan);
		
	}
	
	public static void disconnectVoice(Guild guild, VoiceChannel vc) {
		GuildMusicManager musicManager = getGuildAudioPlayer(guild, leaveTc, leaveMsg, leaveEvent);
		
		musicManager.scheduler.disconnectVoice(guild, vc);
	}
	
	public static void autoPause(Guild gu, GuildVoiceLeaveEvent event) {
		GuildMusicManager musicManager = getGuildAudioPlayer(gu, leaveTc, leaveMsg, leaveEvent);
		
		TextChannel value = musicManager.scheduler.tc;
		boolean isPaused = musicManager.player.isPaused();
		
		if(!isPaused) {
			if(musicManager.scheduler.isPlay == 1) {
				
				pause(value, leaveEvent, null, musicManager.scheduler.setLanguageStop, 0);
				
				String language = ":pause_button: **" + event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getName() + "**�� �ƹ��� ���� �ڵ����� �Ͻ������߾��";
				if(musicManager.scheduler.setLanguageStop.equals("eng")) language = ":pause_button: Auto paused because nobody in **" + event.getChannelLeft().getName() + "**.";
				
				value.sendMessage(language).queue(response -> {
					musicManager.scheduler.autoPausedId = response.getId();
				});
				
				String reply = "BOT: `(" + event.getGuild().getName() + ")` " + language;
				System.out.println(reply);	
				
				if(!event.getMember().getId().equals(BotMusicListener.admin))
					log(value, null, reply);
				
	    		
				musicManager.scheduler.autoPaused = 1;
			}
		}
		
	}
	
	public static void autoPauseToMove(Guild gu, GuildVoiceMoveEvent event) {
		GuildMusicManager musicManager = getGuildAudioPlayer(gu, leaveTc, leaveMsg, leaveEvent);
		
		TextChannel value = musicManager.scheduler.tc;
		boolean isPaused = musicManager.player.isPaused();
		
		String language = ":pause_button: **" + event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getName() + "**�� �ƹ��� ���� �ڵ����� �Ͻ������߾��";
		if(musicManager.scheduler.setLanguageStop.equals("eng")) language = ":pause_button: Auto paused because nobody in **" + event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getName() + "**.";
		
		if(!isPaused) {
			if(musicManager.scheduler.isPlay == 1) {
				pause(value, leaveEvent, null, musicManager.scheduler.setLanguageStop, 0);
				
				musicManager.scheduler.voiceChannelId = gu.getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getId();

				value.sendMessage(language).queue(response -> {
					musicManager.scheduler.autoPausedId = response.getId();
				});
				
				String reply = "BOT: `(" + event.getGuild().getName() + ")` " + language;
				System.out.println(reply);	
				log(value, null, reply);
				
				musicManager.scheduler.autoPaused = 1;
			}
		}
		else {
			if(musicManager.scheduler.autoPaused == 1) {
				if( event.getGuild().getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getId().equals(musicManager.scheduler.voiceChannelId)) return;
				
				musicManager.scheduler.voiceChannelId = gu.getMemberById(BotMusicListener.bot).getVoiceState().getChannel().getId();

				String langu = language;
				Runnable edit = () -> {
					try {
						value.editMessageById(musicManager.scheduler.autoPausedId, langu).complete();
					}
					catch(Exception e) {
						value.sendMessage(langu).queue(response -> {
							musicManager.scheduler.autoPausedId = response.getId();
						});
					}
					
				};
				
				Thread t1 = new Thread(edit);
				t1.start();

			}
		}
		

	}
	
	public static void autoResume(Guild gu, GuildVoiceJoinEvent event, List<Member> members, String valueGuild, GuildVoiceMoveEvent moveEvent) {
		GuildMusicManager musicManager = getGuildAudioPlayer(gu, leaveTc, leaveMsg, leaveEvent);
		
		TextChannel value = musicManager.scheduler.tc;
		boolean isPaused = musicManager.player.isPaused();
		
		if(isPaused) {
			if(musicManager.scheduler.isPlay == 1 && musicManager.scheduler.autoPaused == 1) {
				
			    resume(value, leaveEvent, null, musicManager.scheduler.setLanguageStop, 0);

				Runnable delete = () -> {
					try {
						value.deleteMessageById(musicManager.scheduler.autoPausedId).complete();
					}
					catch(Exception e) {}
					
					if(event == null) {
						if(!moveEvent.getMember().getId().equals(BotMusicListener.admin))
							log(value, null, "BOT: `(" + gu.getName() + ")` �ٽ� ���� �̾ ����ؿ�");
					}
					else {
						if(!event.getMember().getId().equals(BotMusicListener.admin))
							log(value, null, "BOT: `(" + gu.getName() + ")` �ٽ� ���� �̾ ����ؿ�");
					}
	 			};
				
	        	Thread t1 = new Thread(delete);
				t1.start();
				
				musicManager.scheduler.autoPaused = 0;
			}
			else {
				StringBuilder s = new StringBuilder();
	    		StringBuilder t = new StringBuilder();
	    		String title = "";
	    		
	    		if(event == null) {
	    			title = "**(�Ͻ�����) " + moveEvent.getGuild().getName() + "/" + moveEvent.getVoiceState().getChannel().getName() + "**�� �ο� (" + members.size() + "��)\n```css\n";
	    		}
	    		else {
	    			title = "**(�Ͻ�����) " + event.getGuild().getName() + "/" + event.getVoiceState().getChannel().getName() + "**�� �ο� (" + members.size() + "��)\n```css\n";
	    		}
	    		
	    		s.append(title);
	    		t.append(title);
	    		
	        	String valu = valueGuild;
	        	Runnable edit = () -> {
		        	try {
		        		BotMusicListener.voiceTc.editMessageById(valu, BotMusicListener.voiceStats(s, members)).complete();	
		        	}
		        		
		        	catch(Exception e) {
		        		BotMusicListener.voiceTc.sendMessage(BotMusicListener.voiceStats(t, members)).queue(response -> {
		        			BotMusicListener.voiceTcMessage.put(event.getGuild(), response.getId());
		
			   			});
		        	}
	        	};
	        	
	        	Thread t1 = new Thread(edit);
				t1.start();
			}
			
		}
		else {
			StringBuilder s = new StringBuilder();
    		StringBuilder t = new StringBuilder();
    		String title = "";
    		if(event == null)
    			title = "**" + moveEvent.getGuild().getName() + "/" + moveEvent.getVoiceState().getChannel().getName() + "**�� �ο� (" + members.size() + "��)\n```css\n";
    		else 
    			title = "**" + event.getGuild().getName() + "/" + event.getVoiceState().getChannel().getName() + "**�� �ο� (" + members.size() + "��)\n```css\n";
    		
    		s.append(title);
    		t.append(title);
    		
        	String valu = valueGuild;
        	Runnable edit = () -> {
	        	try {
	        		BotMusicListener.voiceTc.editMessageById(valu, BotMusicListener.voiceStats(s, members)).complete();	
	        	}
	        		
	        	catch(Exception e) {
	        		BotMusicListener.voiceTc.sendMessage(BotMusicListener.voiceStats(t, members)).queue(response -> {
	        			BotMusicListener.voiceTcMessage.put(event.getGuild(), response.getId());
	
		   			});
	        	}
        	};
   
        	Thread t1 = new Thread(edit);
        	t1.start();
		}

	}
	
	public static void checkLock(TextChannel channel, User user, MessageReceivedEvent event) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);

		if(musicManager.scheduler.lock == 1) {
			if(user.toString().contains("297963786504110083")) {}
			else {
				channel.sendMessage("������ ����־��").queue();
				BotMusicListener.ret = 1;
			}
		}
	}

	public static void reset(TextChannel channel, Message msg, MessageReceivedEvent event) {
		Guild guild = channel.getGuild();
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild(), channel, msg, event);
		
		musicManager.scheduler.terminate = 1;
		musicManager.scheduler.isPlay = 0;
		
		channel.sendMessage(":clipboard: `ó�� ��...`").queue(response -> {

			if (guild.getAudioManager().isConnected()) {
				guild.getAudioManager().closeAudioConnection();	
				isInTotal = isInTotal - 1;
			}
			
			musicManager.scheduler.clearQueue(channel, msg, response, guild, event, 6, 0, "kor");
			
			
			File file = new File(BotMusicListener.directoryDefault + "guild/removeVoiceStateMessages.txt");
			try {
			    FileWriter fw = new FileWriter(file);
			    fw.write(String.valueOf(isInTotal));
			    fw.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			    response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue(); 
			    log(channel, event, ":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e));
			}
			
			editguilds(channel, msg, event);
		});
		
		
		/*
		if(isInTotal == 0) {
			if(gunBamStateRun == 1) {
				gunBamStateTimer.cancel(); 
				gunBamStateTimer = null;
				gunBamStateRun = 0;		
			}
		}
		*/
		
		musicManager.scheduler.reset(channel);
		
	}
	
	public static String realTitle(String title) {
		title = title.replaceAll("\\*", "\\\\*").replaceAll("_", "\\\\_").replaceAll("~", "\\\\~").replaceAll("`", "\\\\`");
		return title;
	}
	
	public static void getListen(TextChannel channel, Message msg, MessageReceivedEvent event, String vc) {
		
		for(int i = 0; i<listg.size(); i++) {
			
			if(listg.get(i).toString().contains(vc)) {
				channel.sendMessage("**" + listg.get(i).toString() + "** �� �ؽ�Ʈ ä�ε�\n```" + listg.get(i).getTextChannels().toString() + "```").queue();
			}
		}
	}

	public static void log(TextChannel tc, MessageReceivedEvent event, String str) {
		if(BotMusicListener.logOn == 1) {
			if(event == null) {
				if(tc.getGuild().toString().contains(BotMusicListener.base)||tc.getId().equals("717203670365634621")) {}
				else BotMusicListener.logtc.sendMessage(str).queue();
			}
			else {
				if(event.getAuthor().getId().equals(BotMusicListener.admin)||tc.getGuild().toString().contains(BotMusicListener.base)||tc.getId().equals("717203670365634621")) {}
				else BotMusicListener.logtc.sendMessage(str).queue();
			}
		}
		
		File file = new File(BotMusicListener.directoryDefault + "log.txt");
		
		try {
		      FileWriter fw = new FileWriter(file, true);
		      fw.append("\n" + str);
		      
		      fw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			BotMusicListener.logtc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + staticCause(e)).queue();
		}
	}
	
	
}