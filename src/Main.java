package src;

import java.util.HashMap;

public class Main {
 public static void main(String[] args){
    printMainTitle();
    printMainStoryStart();
        }
    public static void printMainTitle(){
        System.out.println("________________________________________________________________________________________________________________");
        System.out.println();
        System.out.println(" ▄▄▄        █████▒▄▄▄█████▓▓█████  ██▀███        ▄▄▄█████▓ ██░ ██ ▓█████         █████▒▄▄▄       ██▓     ██▓    \r\n" + //
                        "▒████▄    ▓██   ▒ ▓  ██▒ ▓▒▓█   ▀ ▓██ ▒ ██▒      ▓  ██▒ ▓▒▓██░ ██▒▓█   ▀       ▓██   ▒▒████▄    ▓██▒    ▓██▒    \r\n" + //
                        "▒██  ▀█▄  ▒████ ░ ▒ ▓██░ ▒░▒███   ▓██ ░▄█ ▒      ▒ ▓██░ ▒░▒██▀▀██░▒███         ▒████ ░▒██  ▀█▄  ▒██░    ▒██░    \r\n" + //
                        "░██▄▄▄▄██ ░▓█▒  ░ ░ ▓██▓ ░ ▒▓█  ▄ ▒██▀▀█▄        ░ ▓██▓ ░ ░▓█ ░██ ▒▓█  ▄       ░▓█▒  ░░██▄▄▄▄██ ▒██░    ▒██░    \r\n" + //
                        " ▓█   ▓██▒░▒█░      ▒██▒ ░ ░▒████▒░██▓ ▒██▒        ▒██▒ ░ ░▓█▒░██▓░▒████▒      ░▒█░    ▓█   ▓██▒░██████▒░██████▒\r\n" + //
                        " ▒▒   ▓▒█░ ▒ ░      ▒ ░░   ░░ ▒░ ░░ ▒▓ ░▒▓░        ▒ ░░    ▒ ░░▒░▒░░ ▒░ ░       ▒ ░    ▒▒   ▓▒█░░ ▒░▓  ░░ ▒░▓  ░\r\n" + //
                        "  ▒   ▒▒ ░ ░          ░     ░ ░  ░  ░▒ ░ ▒░          ░     ▒ ░▒░ ░ ░ ░  ░       ░       ▒   ▒▒ ░░ ░ ▒  ░░ ░ ▒  ░\r\n" + //
                        "  ░   ▒    ░ ░      ░         ░     ░░   ░         ░       ░  ░░ ░   ░          ░ ░     ░   ▒     ░ ░     ░ ░   \r\n" + //
                        "      ░  ░                    ░  ░   ░                     ░  ░  ░   ░  ░                   ░  ░    ░  ░    ░  ░\r" + //
                                                        "");//ART by
        System.out.println("________________________________________________________________________________________________________________");
    }
    public static void printMainStoryStart(HashMap<String, Room> rooms) {
        System.out.println();
        System.out.println("Ten years ago, the world was forever changed...");
        System.out.println();
        System.out.println("It all began with a mysterious virus outbreak. The virus spread rapidly, turning humans into mindless, flesh-eating creatures.");
        System.out.println("Cities fell, governments collapsed, and civilization as we knew it crumbled.");
        System.out.println();
        System.out.println("Now, ten years later, the world is a desolate wasteland. Small pockets of survivors struggle to stay alive,");
        System.out.println("fighting off the relentless hordes of the undead and scavenging for resources.");
        System.out.println();
        System.out.println("In this harsh new reality, trust is a rare commodity, and danger lurks around every corner.");
        System.out.println("But amidst the darkness, a glimmer of hope remains. A group of survivors has discovered a potential cure,");
        System.out.println("and they embark on a perilous journey to save humanity from the brink of extinction.");
        System.out.println();
        System.out.println("This is their story...");//Text by CHATGDP 4o

        String name_ = "defaultRoomName";
        String description_;
        name_ = rooms.get(name_).getName();
        description_ = rooms.get(name_).getDescription();
        System.out.println(name_);
          System.out.println(description_);
    }
    
    }

