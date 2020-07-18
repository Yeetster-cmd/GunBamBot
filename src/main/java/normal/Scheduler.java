package normal;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import functions.*;
import main.BotMusicListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Scheduler extends CustomFunctions {
	
	TextChannel tc;
	Message msg;
	MessageReceivedEvent event;
	User user;
	
	String userNormalTimerId = "", botNormalTimerId = "", userNowTimerId = "";
	String newsUserMessageId = "", newsBotMessageId = "", newsBotMessage2Id = "";
	String helpUserMessageId = "", helpBotMessageId = "";
	String kospiUserMessageId = "", kospiBotMessageId = "";
	String specUserMessageId = "", specBotMessageId = "";
	String infoUserMessageId = "", infoBotMessageId = "";
	String uptimeUserMessageId = "", uptimeBotMessageId = "";
	String cpuUserMessageId = "", cpuBotMessageId = "";
	
	String cpu = "[Intel(R) Core(TM) i7-10510U](https://ark.intel.com/content/www/kr/ko/ark/products/196449/intel-core-i7-10510u-processor-8m-cache-up-to-4-90-ghz.html)";
	
	Timer normalTimer = null;
	Timer uptimeTimer = null;
	Timer cpuTimer = null;
	
	int timeLeft = 0;
	int timeUp = 0;
	
	int normalRun = 0;
	int uptimeRun = 0;
	int uptimeCounting = 0;
	
	int cpuRun = 0;
	int cpuCounting = 0;

	
	public Scheduler(Guild guild, TextChannel tc, Message msg, MessageReceivedEvent event) {
		this.tc = tc;
		this.msg = msg;
		this.event = event;
	}
	
	
	public void setTimer(User user, TextChannel tc, Message msg, MessageReceivedEvent event, int time) {
		this.user = user;
		
		timeLeft = time;
		
		timeUp = 0;
		
		userNormalTimerId = autoRemoveMessage(tc, msg, userNormalTimerId, botNormalTimerId);
		
		if(normalRun == 1) {
			normalTimer.cancel();
			normalTimer = null;
			normalRun = 0;
		}
		
		if(normalTimer == null)
			normalTimer = new Timer();
		normalTimer(tc, msg, user, event);
	        
		String reply = "BOT: **" + time + "��** ���Ҿ��";
        tc.sendMessage("**" + time + "��** ���Ҿ��").queue(response -> {
        	botNormalTimerId = response.getId();
        });
        System.out.println(reply);
        log(tc, event, reply);
		
        normalRun = 1;
     
	}
	
	public void nowTimer(TextChannel tc, Message msg) {
		
		userNowTimerId = autoRemoveMessage(tc, msg, userNowTimerId, botNormalTimerId);
		
		if(normalRun == 0) {
			String reply = "BOT: Ÿ�̸Ӹ� �������� �ʾҾ��";
			tc.sendMessage("Ÿ�̸Ӹ� �������� �ʾҾ��").queue();
		    System.out.println(reply);
		    log(tc, event, reply);
				
		}
			
		else {
			String reply = "BOT: **" + (int)(timeLeft - timeUp) + "��** ���Ҿ��";
		    tc.sendMessage("**" + (int)(timeLeft - timeUp) + "��** ���Ҿ��").queue(response -> {
		    	botNormalTimerId = response.getId();
		    });
		    System.out.println(reply);
		    log(tc, event, reply);
				
		}
    }

	
	public void timerCancel(TextChannel tc, Message msg) {
		
		if(timeLeft == 0) {
			String reply = "BOT: ������ Ÿ�̸Ӱ� �����";
			tc.sendMessage("������ Ÿ�̸Ӱ� �����").queue();
			System.out.println(reply);
			log(tc, event, reply);
		}
		
		else {
			normalTimer.cancel();
			normalTimer = null;
			timeLeft = 0;
			timeUp = 0;
			normalRun = 0;
	    	
	    	String reply = "BOT: Ÿ�̸Ӹ� ����߾��";
			tc.sendMessage("Ÿ�̸Ӹ� ����߾��").queue();
			System.out.println(reply);
			log(tc, event, reply);
		}
		
	}
	
	//task
	public void normalTimer(TextChannel tc, Message msg, User user, MessageReceivedEvent event) {
		
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
            	Runnable edit = () -> {
	            	timeUp = timeUp + 1;
	
	            	try {
	            		tc.editMessageById(botNormalTimerId, "**" + (int)(timeLeft - timeUp) + "��** ���Ҿ��").complete();
	            	}
	            		
	            	catch(Exception e) {
	            		tc.sendMessage("**" + (int)(timeLeft - timeUp) + "��** ���Ҿ��").queue(response -> {
	            			botNormalTimerId = response.getId();
	            		});
	            	}
	
	            	if(timeUp == timeLeft) {
	            		
	            		normalTimer.cancel();
	            		normalTimer = null;
	   		
	            		removeMessage(tc, userNormalTimerId, botNormalTimerId);
	            		
			        	String reply = "BOT: <@" + user.getId() + ">  **" + timeUp + "��** Ÿ�̸Ӱ� ����Ǿ����";
						tc.sendMessage("<@" + user.getId() + ">  **" + timeUp + "��** Ÿ�̸Ӱ� ����Ǿ����").queue();
						System.out.println(reply);
						log(tc, event, reply);
						
						timeLeft = 0;
	            		timeUp = 0;
	            		normalRun = 0;
	
	            	}
            	};
            	Thread t1 = new Thread(edit);
            	t1.start();

            }
        };

        normalTimer.scheduleAtFixedRate(task, 60000, 60000);
        	
	}
	

	public void uptime(TextChannel tc, Message msg, MessageReceivedEvent event, int uptime, String lan) {

		if(uptimeRun == 1) {
			uptimeTimer.cancel();
			uptimeTimer = null;
			uptimeRun = 0;
		}
		
		uptimeUserMessageId = autoRemoveMessage(tc, msg, uptimeUserMessageId, uptimeBotMessageId);
		
		String language = "���� **" + uptimeFormat(uptime, lan) + "** ���� ���� ���̿���";
		if(lan.equals("eng"))
			language = "Uptime is **" + uptimeFormat(uptime, lan) + "**";
		
    	tc.sendMessage(language).queue(response -> {
    		uptimeBotMessageId = response.getId();
    	});
    	
    	if(uptimeTimer == null)
			uptimeTimer = new Timer();
    	
    	uptimeCounting = 0;
		uptimeTimer(tc, msg, event, lan);
		uptimeRun = 1;
    	
    	
	}
	
	public void uptimeTimer(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		
		TimerTask task = new TimerTask() {
            @Override
            public void run() {
   
            	if(uptimeCounting < 15) {
	            	long nowTime = System.currentTimeMillis();
	            	String language = "���� **" + uptimeFormat((int)(nowTime - BotMusicListener.gunbamStartTime), lan) + "** ���� ���� ���̿���";
	            	if(lan.equals("eng"))
	            		language = "Uptime is **" + uptimeFormat((int)(nowTime - BotMusicListener.gunbamStartTime), lan) + "**";
		            
	            	String lang = language;
	            	Runnable edit = () -> {
		            	try {
			            	tc.editMessageById(uptimeBotMessageId, lang).complete();
			            }
			            		
			            catch(Exception e) {
			            	if(uptimeRun == 1) {
			            		uptimeTimer.cancel();
			            		uptimeTimer = null;
			            		uptimeRun = 0;
			            	}
			            }
	            	};
	                Thread t1 = new Thread(edit);
	                t1.start();
		            	
	                uptimeCounting = uptimeCounting + 3;
            	}
            	else {
            		if(uptimeRun == 1) {
	            		uptimeTimer.cancel();
	            		uptimeTimer = null;
	            		uptimeRun = 0;
	            	}
            	}
            		
	
            }
        };

        uptimeTimer.scheduleAtFixedRate(task, 3000, 3000);
	}
	
	public String uptimeFormat(int getTimes, String lan) { 
		
		getTimes = getTimes/1000;
        int day, hour, min, sec;

        sec  = getTimes % 60;
        min  = getTimes / 60 % 60;
        hour = getTimes / 3600;
        day = hour / 24;
        hour = hour - 24*day;
        
        String s = "";
        String dayStr = String.valueOf(day);
        String hourStr = String.valueOf(hour);
        String minStr = String.valueOf(min);
        String secStr = String.valueOf(sec);
        
        if(day < 1) {
        	s = hourStr + "�ð� " + minStr + "�� " + secStr + "��";
        }
        else 
        	s = dayStr + "�� " + hourStr + "�ð� " + minStr + "�� " + secStr + "��";
		
        if(lan.equals("eng")) {
        	if(hour < 10) hourStr = "0" + hourStr;
        	if(min < 10) minStr = "0" + minStr;
        	if(sec < 10) secStr = "0" + secStr;
        	
        	s = dayStr + ":" + hourStr + ":" + minStr + ":" + secStr;
        }
        
		return s;
	}

	public void useCpu(TextChannel tc, Message msg, String lan) {
		if(cpuRun == 1) {
			cpuTimer.cancel();
			cpuTimer = null;
			cpuRun = 0;
		}
		
		cpuUserMessageId = autoRemoveMessage(tc, msg, cpuUserMessageId, cpuBotMessageId);
		
		EmbedBuilder eb = new EmbedBuilder();
		
		String title = "�ý��� ����";
		String cpuCountStr = "���μ���";
		String usageStr = "��뷮";
		String ramStr = "RAM ��뷮";
		
		if(lan.equals("eng")) {
			title = "System Info";
			cpuCountStr = "Processor";
			usageStr = "Usage";
			ramStr = "RAM";
		}
		
		eb.setColor(Color.decode(BotMusicListener.colorDefault));
		eb.setTitle(title);
		eb.addField("CPU", cpu, false);
		eb.addField(cpuCountStr, "\n", true);
		eb.addField(usageStr, "\n", true);
		eb.addField(ramStr, "\n", true);
		eb.setFooter("Calculating...");

		tc.sendMessage(eb.build()).queue(response -> {
			cpuBotMessageId = response.getId();
			response.editMessage(showCpu(tc, lan).build()).queue();
		});
		
		if(cpuTimer == null)
			cpuTimer = new Timer();
   
		cpuCounting = 0;
		
		cpuTimer(tc, msg, lan);
		cpuRun = 1;
	
	}
	
	public void cpuTimer(TextChannel tc, Message msg, String lan) {
		TimerTask task = new TimerTask() {
			@Override
            public void run() {
            	
				if(tc.getGuild().getJDA().getStatus().toString().equals("CONNECTED")) {
				
	            	if(cpuCounting < 30) {
		            
		        		Runnable r = () -> {
		        			if(!tc.getGuild().getId().equals(BotMusicListener.base))
			        			cpuCounting = cpuCounting + 3;
			     
		        			try {
		        				tc.editMessageById(cpuBotMessageId, showCpu(tc, lan).build()).complete();
		        			}
		        			catch(Exception e) {
		        				if(cpuRun == 1) {
		        					cpuTimer.cancel();
		        					cpuTimer = null;
		        					cpuRun = 0;
		        				}
		        				
		        				removeMessage(tc, cpuUserMessageId, cpuBotMessageId);

		        			}
		        		};
		        		Thread t = new Thread(r);
		        		t.start();
		
	            	}
	            	else {
	            		
	            		if(cpuRun == 1) {
	    					cpuTimer.cancel();
	    					cpuTimer = null;
	    					cpuRun = 0;
	    				}
	            		
	            		removeMessage(tc, cpuUserMessageId, cpuBotMessageId);
	            		
	            	}
				}
            }
        };
        

        cpuTimer.scheduleAtFixedRate(task, 3000, 3000);
	}
	
	@SuppressWarnings("deprecation")
	public EmbedBuilder showCpu(TextChannel tc, String lan) {
		
		com.sun.management.OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
		
		EmbedBuilder eb = new EmbedBuilder();
		
		String title = "�ý��� ����";
		String cpuCountStr = "���μ���";
		String usageStr = "��뷮";
		String ramStr = "RAM ��뷮";
		
		if(lan.equals("eng")) {
			title = "System Info";
			cpuCountStr = "Processor";
			usageStr = "Usage";
			ramStr = "RAM";
		}
		
		String usePercent = "";
		double cpuLoad = osBean.getSystemCpuLoad() * 100;
		if(cpuLoad == 100)
			usePercent = String.format("%.0f", cpuLoad) + "%";
		else
			usePercent = String.format("%.1f", cpuLoad) + "%";
		
		eb.setTitle(title);
		eb.addField("CPU", cpu, false);
		eb.addField(cpuCountStr, String.format("%.0f", (double)(osBean.getAvailableProcessors())), true);
		eb.addField(usageStr, usePercent, true);
		eb.addField(ramStr, String.format("%.2f", (double)((osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize())/1024f/1024f/1024f)) + "GB / " + String.format("%.1f", (double)(osBean.getTotalPhysicalMemorySize()/1024f/1024f/1024f)) + "GB", true);
		
		if((double)(osBean.getSystemCpuLoad() * 100) >= 80 || (double)(osBean.getFreePhysicalMemorySize()/1024f/1024f/1024f) <= 4f)
			eb.setColor(Color.RED);

		else if((double)(osBean.getSystemCpuLoad() * 100) >= 50 || (double)(osBean.getFreePhysicalMemorySize()/1024f/1024f/1024f) <= 8f)
			eb.setColor(Color.YELLOW);
		
		else if((double)(osBean.getSystemCpuLoad() * 100) >= 10)
			eb.setColor(Color.GREEN);
		
		else
			eb.setColor(Color.decode(BotMusicListener.colorDefault));
		
		if(!tc.getGuild().getId().equals(BotMusicListener.base)) {
			int leftSec = (int)(30 - cpuCounting);
			
			String leftStr = String.valueOf(leftSec);
			if(leftSec < 10) {
				leftStr = "0" + leftSec;
			}
			
			eb.setFooter("- 0:" + leftStr);
		}
		else {
			long nowTime = System.currentTimeMillis();
			eb.setFooter(uptimeFormat((int)(nowTime - BotMusicListener.gunbamStartTime), "eng") + "");
		}
		
		return eb;
	}
	
	public void news(TextChannel tc, Message msg) {
		
		newsUserMessageId = autoRemoveMessage(tc, msg, newsUserMessageId, newsBotMessageId, newsBotMessage2Id);
		
		File file = new File(BotMusicListener.directoryDefault + "news.txt");
		MessageBuilder s = new MessageBuilder();
		try {
			FileReader filereader = new FileReader(file);
		       
		    BufferedReader bufReader = new BufferedReader(filereader);
		    String line = "";
		       
		    while((line = bufReader.readLine()) != null){
		    	s.append(line + "\n");
		    }
		               
		    bufReader.close();
		}
		catch(Exception e) {
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue(response -> {
				newsBotMessageId = response.getId();
			});
			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
		
		}
			
		tc.sendMessage(s.build()).queue(response -> {
			newsBotMessageId = response.getId();
		});
		System.out.println("BOT: \n" + s);			

		File file2 = new File(BotMusicListener.directoryDefault + "news2.txt");
		MessageBuilder s2 = new MessageBuilder();
		try {
			FileReader filereader2 = new FileReader(file2);
		       
		    BufferedReader bufReader2 = new BufferedReader(filereader2);
		    String line2 = "";
		       
		    while((line2 = bufReader2.readLine()) != null){
		        s2.append(line2 + "\n");
		    }
		               
		    bufReader2.close();
		}
		catch(Exception e) {
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue(response -> {
				newsBotMessage2Id = response.getId();
			});
			log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
	
		}
			
		tc.sendMessage(s2.build()).queue(response -> {
			newsBotMessage2Id = response.getId();
		});
			
		System.out.println("BOT: \n" + s2);

	}
	
	public void kospi(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		
		kospiUserMessageId = autoRemoveMessage(tc, msg, kospiUserMessageId, kospiBotMessageId);
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.decode(BotMusicListener.colorDefault));
		
		String language = "�ڽ��� �����ҷ����� ��...";
		
		if(lan.equals("eng")) 
			language = "Getting KRX: Kospi...";
		
		eb.setTitle(language);
	    tc.sendMessage(eb.build()).queue(response -> { 
	    	kospiBotMessageId = response.getId();
			StringBuilder m = new StringBuilder();
			StringBuilder n = new StringBuilder();
			StringBuilder o = new StringBuilder();
					
			URL urlm;
	
			try {
				urlm = new URL("https://m.stock.naver.com/sise/siseList.nhn?menu=market_sum&sosok=0");
				BufferedReader br = new BufferedReader(new InputStreamReader(urlm.openStream(), "utf-8"));
				String line = "";
				int checkLine = 0;
						
				while((line = br.readLine()) != null) {
					if(line.contains("<a href=\"/sise/siseIndex.nhn?code=KOSPI\""))
					checkLine = 1;
						
					if(line.contains("https://ssl.pstatic.net/imgfinance/chart/mobile/mini/KOSPI")) 
						checkLine = 0;
							
					if(checkLine == 1) {
								
						if(line.contains("<span class=\"stock_price")) {
							String temp2 = line.split(">")[1].split("<")[0];
									
							n.append(temp2);
									
						}
								
						if(line.contains("<span class=\"gap_price\">")) {
							String temp3 = line.split("<em class=\"ico\">")[1].split("</em>")[0];
							String temp4 = line.split("</em>")[1].split("<")[0];
									
							if(temp3.contains("��")) temp3 = "+";
							else if(temp3.contains("��")) temp3 = "-";
							n.append(" `(" + temp3); //��ȣ
							n.append(temp4 + ")`"); //val
									
						}
								
					}				
				}
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
	       		log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
			}
			    	
			try {
		
				URL url = new URL("https://m.stock.naver.com/marketindex/index.nhn?menu=oilgold#oilgold");
						
				BufferedReader br2 = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
				BufferedReader br3 = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
						
				int checkLine = 0;
				String line = "";
				while((line = br2.readLine()) != null) {
					if(line.contains("<a href=\"/marketindex/item.nhn?marketindexCd=FX_USDKRW&menu=exchange\""))  //FX_USDKRW
						checkLine = 1;
						
					if(line.contains("<a href=\"/marketindex/item.nhn?marketindexCd=FX_EURKRW&menu=exchange\"")) 
						checkLine = 0;
							
					if(checkLine == 1) {
								
						if(line.contains("<span class=\"stock_price\">")) {
							String temp2 = line.split("<span class=\"stock_price\">")[1].split("<")[0];
							m.append(temp2 + "��");
						}
								
						if(line.contains("<span class=\"gap_price\">")) {
								
							String temp3 = line.split("<em class=\"ico\">")[1].split("</em>")[0];
					
							String temp4 = line.split("</em>")[1].split("</span>")[0];
							temp4 = temp4.replaceAll(" ", "");
	
							if(temp3.contains("��")) temp3 = "+";
							else if(temp3.contains("��")) temp3 = "-";
									
							m.append(" `(" + temp3 + temp4 + ")`");
									
						}
					}	
				}
						
				line = "";
				while((line = br3.readLine()) != null) {
					if(line.contains("<a href=\"/marketindex/item.nhn?marketindexCd=CMDT_GD&menu=oilgold\""))  //�ݽü�
						checkLine = 1;
						
					if(line.contains("<a href=\"/marketindex/item.nhn?marketindexCd=CMDT_GC&menu=oilgold\"")) 
						checkLine = 0;
							
					if(checkLine == 1) {
								
						if(line.contains("<span class=\"stock_price\">")) {
							String temp2 = line.split("<span class=\"stock_price\">")[1].split("</span>")[0];
							o.append(temp2 + "��");
									
						}
								
						if(line.contains("<span class=\"gap_price\">")) {
								
							String temp3 = line.split("<span class=\"ico\">")[1].split("</span>")[0];
					
							String temp4 = line.split("</span>")[1].split("</span>")[0];
							temp4 = temp4.replaceAll(" ", "");
	
							if(temp3.contains("��")) temp3 = "+";
							else if(temp3.contains("��")) temp3 = "-";
									
							o.append(" `(" + temp3 + temp4 + ")`");
									
						}
					}	
				}
						
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.editMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
	           	log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
			}
	
			String languageTitle = "�ѱ��� �ڽ��� ��Ȳ";
			String languageKospi = "�ڽ��� ����";
			String languageDollar = "1 �̱� �޷�";
			String languageGold = "�� 1g";
			
			if(lan.equals("eng")) {
				languageTitle = "Korea Stock Price";
				languageKospi = "KOSPI";
				languageDollar = "1 US Dollar";
				languageGold = "1g gold";
			}
				
			EmbedBuilder eb2 = new EmbedBuilder();
			eb2.setColor(Color.decode(BotMusicListener.colorDefault));
			
			eb2.setTitle(languageTitle);
			eb2.addField(languageKospi, n.toString(), true);
			eb2.addField(languageDollar, m.toString(), true);
			eb2.addField(languageGold, o.toString(), false);
			response.editMessage(eb2.build()).queue();
			
		});

	}
	
	public void showInfo(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		
		infoUserMessageId = autoRemoveMessage(tc, msg, infoUserMessageId, infoBotMessageId);
		
		String language = "������ ���� ������ �������\n```\n����\n  * ä�� ����Ʈ���� ����: " + BotMusicListener.chatVersion + "\n  * ���� ����: " + BotMusicListener.musicVersion + "\n\n�����\n  * ��Ÿ ���: 2019�� 12�� 31��\n  * ���� ���: 2020�� 1�� 7��\n  * �ѱ� ���: 2022�� 2�� 1��\n\nDEVELOPED by Arrge (powered by JAVA)```����: https://discord.gg/KDstCT2\n";
		if(lan.equals("eng")) language = "Show version info\n```\nVersion\n  * version: " + BotMusicListener.musicVersion + "\n\nRelease info\n  * Beta: 2019.12.31\n  * Alpha: 2020.1.7\n  * Release date: 2020.2.27\n\nDEVELOPED by Arrge (powered by JAVA)```Contact: https://discord.gg/KDstCT2\n";
		tc.sendMessage(language).queue(response -> {
			infoBotMessageId = response.getId();
		});
		
    	System.out.println("BOT: ������ ���� ������ �������\n```����\n  * ä�� ����Ʈ���� ����: " + BotMusicListener.chatVersion + "\n  * ���� ����: " + BotMusicListener.musicVersion + "\n\n�����\n  * ��Ÿ ����: 2019�� 12�� 31��\n  * ���� ���: 2020�� 1�� 7��\n  * �ѱ� ���: 2022�� 2�� 1��\n\nDEVELOPED by Arrge (powered by JAVA)```����: https://discord.gg/KDstCT2\n");
        
	}
	
	
	public void help(TextChannel tc, Message msg, MessageReceivedEvent event, String lan) {
		helpUserMessageId = autoRemoveMessage(tc, msg, helpUserMessageId, helpBotMessageId);
		
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
			tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
		}
		
		String prefix = "��";
		if(msg.getContentRaw().startsWith("$")) prefix = "$";

			
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.decode(BotMusicListener.colorDefault));		
		
		if(tc.getGuild().getId().equals(BotMusicListener.born)) {
			eb.setTitle("����+ ����");
		}
		else {
			eb.setTitle("���� ����");
		}
		
		
		String des = "��� ��ɾ� �տ��� �ѱ��� `��` �Ǵ� `$` �� �����ؿ�";
		if(lan.equals("eng"))
			des = "Prefix is `��` or `$`";
			
		eb.setDescription(des);
		
		String title1 = "����";
		if(lan.equals("eng"))
			title1 = "Info";
			
		String add = "";
		
		if(!BotMusicListener.prefix.equals("arrgeNull"))
			add = "\n-" + prefix + BotMusicListener.prefix;
		
		String value1 = "-" + prefix + "����: `��� ����ϴ��� �������` "
					+ "\n-" + prefix + "��: `�����ð��� �������` "
					+ "\n-" + prefix + "�ڽ���: `�ѱ��� �ڽ���, �̱� �޷��� �������` "
					+ "\n-" + prefix + "ä��: `����ä�� �� ������ �ִ��� �������` "
					+ "\n-" + prefix + "����: `������ ���� ������ �˷����`" 
					+ "\n-" + prefix + "��Ÿ��: `���� �����ð��� �������`" 
					+ "\n-" + prefix + "��뷮: `�������� CPU, RAM ��뷮�� 30�ʰ� �������`" 
					+ "\n-" + prefix + "�Ŀ�: `�Ŀ��� ������ �ּҸ� �˷����`" + add;
			
		if(lan.equals("eng"))
			value1 = "-" + prefix + "help: `showing how to use.` "
				 + "\n-" + prefix + "ping: `showing latency.` "
				 + "\n-" + prefix + "kospi: `showing KRX: KOSPI and 1 US Dollar.` "
				 + "\n-" + prefix + "channel: `showing Gunbam joined the voice channels count.` "
				 + "\n-" + prefix + "info: `showing Gunbam's info.`"
				 + "\n-" + prefix + "uptime: `showing Gunbam's active time.`"
				 + "\n-" + prefix + "uptime: `showing Gunbam's CPU, RAM usage during 30 seconds.`"
				 + "\n-" + prefix + "donate: `showing paypal url.1";
	 
		eb.addField(title1, value1, false);
		
		String title2 = "�÷��̾�";
		if(lan.equals("eng"))
			title2 = "Player";
		
		String value2 = "-" + prefix + "��� [URL|�ܾ�]: `URL�� ����ؿ� (https�� ��)`"
				+ "\n-" + prefix + "������� [������ URL]: `URL�� �������� ���� �� ����ؿ� (https�� ��)`"
				+ "\n-" + prefix + "�˻� [�ܾ�]: `��Ʃ�� �˻���� 7���� �������`"
				+ "\n-" + prefix + "����: `�÷��̾ �Ͻ������ؿ�`"
				+ "\n-" + prefix + "�簳: `�÷��̾ �簳�ؿ�`"
				+ "\n-" + prefix + "��ŵ [����]: `���� �׸����� �뷡�� �ǳʶپ��`"
				+ "\n-" + prefix + "����: `�������� �����`"
				+ "\n-" + prefix + "ť [����]: `�������� ������ 10���� ǥ���ؿ�`"
				+ "\n-" + prefix + "����: `���� �׸��� �ǽð����� �˷����`"
				+ "\n-" + prefix + "������: `����ä���� ������`";
		
		if(lan.equals("eng"))
			value2 = "-" + prefix + "play [URL|query]: `Playing URL. (Only https)`"
				 + "\n-" + prefix + "randomplay [Playlist URL]: `Playing URL links to a playlist after shuffle.`"
				 + "\n-" + prefix + "search [query]: `showing 7 items from Youtube search result.`"
				 + "\n-" + prefix + "pause: `pausing this player.`"
				 + "\n-" + prefix + "resume: `resuming this player.`"
				 + "\n-" + prefix + "skip [num]: `skipping to num.`"
				 + "\n-" + prefix + "shuffle: `shuffle this playlist.`"
				 + "\n-" + prefix + "queue [page]: `showing 10 items in queue.`"
				 + "\n-" + prefix + "nowplay: `showing now playing item.`"
				 + "\n-" + prefix + "stop: `leaving the voice channel.`";
	
		eb.addField(title2, value2, false);
		
		String title3 = "�÷��̾� ����";
		if(lan.equals("eng"))
			title3 = "Setting player";
			
		
		String value3 = "-" + prefix + "�̵� [����ä��id]: `�Է��� id�� �ش��ϴ� ����ä�η� �̵��ؿ�`"
					+ "\n-" + prefix + "Ÿ�̸� [����]: `�ڵ� ���� �ð��� �����ؿ�`"
					+ "\n-" + prefix + "������ [����]: `�Է��� ��°�� �׸��� ������ ����ä���� ������`"
					+ "\n-" + prefix + "���� [����]: `�÷��̾� ������ �����ؿ�`"
					+ "\n-" + prefix + "���� [����]: `���� �� �׸��� ��Ͽ��� �����ؿ�`"
					+ "\n-" + prefix + "���: `��� �� �۾��� �ǵ�����`";
			
		if(lan.equals("eng"))
			value3 = "-" + prefix + "move [voice channel id]: `moving to voice channel if id is vaild`"
				 + "\n-" + prefix + "timer [minute]: `setting aufo off.`"
				 + "\n-" + prefix + "last [num]: `when no.[num] item finished, it will leave the voice channel.`"
				 + "\n-" + prefix + "volume [num]: `Setting the player volume.`"
				 + "\n-" + prefix + "remove [num]: `removing no.[num] item.`"
				 + "\n-" + prefix + "cancel: `cancel.`";
		 
		eb.addField(title3, value3, false);
		
		
		
		if(tc.getGuild().getId().equals(BotMusicListener.born)) {
			String title4 = "�߰����";
			if(lan.equals("eng"))
				title4 = "Plus";
			
			String value4 = "-" + prefix + "�ٽ����: `������ ��ɾ�� �������� �ʱ�ȭ �Ǿ��� ���� ���� ��ɾ��`"
					   + "\n-" + prefix + "������: `" + prefix + "�ٽ�������� ����� �׸��� �������`"
					   + "\n-" + prefix + "����: `�ڱ⸸�� ���Ͽ� ���� �׸��� �����ؿ�`"
					   + "\n-" + prefix + "�ҷ�����: `�ڱ⸸�� ���Ͽ� �����߾��� �׸���� �ҷ��Ϳ�`";
			
			if(lan.equals("eng"))
				value4 = "-" + prefix + "playagain: `this command is when the playlist is initialized with the " + prefix + "stop command.`"
					 + "\n-" + prefix + "savelist: `showing items " + prefix + "playagain.`"
					 + "\n-" + prefix + "save: `save queue at your file.`"
					 + "\n-" + prefix + "load: `load saved items from your file.`";
		 
			eb.addField(title4, value4, false);
		}
		
		if(lan.equals("eng")) {}
		else eb.addField("�ΰ����", "- ��¥����� �� �� �־��\n- �� ������ ����� �ý��� �ð��� �˷����", false);
		
		String footer = "����: " + BotMusicListener.musicVersion + "  |  " + tc.getJDA().getGuilds().size() + "�� ����";
		if(lan.equals("eng"))
			footer = "Version: " + BotMusicListener.musicVersionEng + "  |  " + tc.getJDA().getGuilds().size() + " servers";
		eb.setFooter(footer);
		
			
		tc.sendMessage(eb.build()).queue(response -> {
			helpBotMessageId = response.getId();
		});
		
			
		System.out.println(des);
	}

	public void log(TextChannel tc, MessageReceivedEvent event, String str) {
		if(BotMusicListener.logOn == 1) {
			if(event.getAuthor().getId().equals(BotMusicListener.admin)||tc.getGuild().toString().contains(BotMusicListener.base)) {}
			else BotMusicListener.logtc.sendMessage(str).queue();
		}
		
		File file = new File(BotMusicListener.directoryDefault + "log.txt");
		
		try {
		      FileWriter fw = new FileWriter(file, true);
		      fw.append("\n" + str);
		      
		      fw.close();
		} 
		catch (Exception e) {
		      e.printStackTrace();
		      tc.sendMessage(":no_entry_sign: **" + e.getMessage() + "**" + cause(e)).queue();
 			  log(tc, event, ":no_entry_sign: **" + e.getMessage() + "**" + cause(e));
		}
	}
	
	
	public void reset(TextChannel tc) {
		newsUserMessageId = ""; newsBotMessageId = ""; newsBotMessage2Id = "";
		helpUserMessageId = ""; helpBotMessageId = "";
		kospiUserMessageId = ""; kospiBotMessageId = "";
		specUserMessageId = ""; specBotMessageId = "";
		uptimeUserMessageId = ""; uptimeBotMessageId = "";
	}
	
}
