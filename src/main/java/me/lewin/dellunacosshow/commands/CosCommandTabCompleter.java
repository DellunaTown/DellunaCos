package me.lewin.dellunacosshow.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CosCommandTabCompleter implements TabCompleter {
    List<String> empty = new ArrayList<String>() {{ add(""); }};

    String[] en_commands = { "enter", "add", "set", "remove" };

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.isOp()) return empty;
        if (args.length > 0) {
            switch (args[0]) {
                case "enter":
                    return empty;

                case "set":
                    if (args.length == 2) {
                        ArrayList<String> list = new ArrayList<>();
                        list.add("viewpoint");
                        list.add("npc");
                        list.add("out");
                        return tabCompleteSort(list, args[1]);
                    }
                    else
                        return empty;

                case "add":
                case "추가":
                    if (args.length == 2) {
                        ArrayList<String> list = new ArrayList<>();
                        list.add("new");
                        list.add("tool");
                        list.add("hat");
                        list.add("weapon");
                        list.add("etc");
                        return tabCompleteSort(list, args[1]);
                    }
                    else if (args.length == 3) {
                        ArrayList<String> list = new ArrayList<>();
                        list.add("COSMETIC_HEAD");
                        list.add("COSMETIC_HAND");
                        return tabCompleteSort(list, args[2]);
                    }
                    else if (args.length == 4) {
                        ArrayList<String> list = new ArrayList<>();
                        list.add("true");
                        list.add("false");
                        return tabCompleteSort(list, args[3]);
                    }
                    else if (args.length == 5 && args[1].equals("new")) {
                        ArrayList<String> list = new ArrayList<>();
                        list.add("<index 0~20>");
                        return tabCompleteSort(list, args[4]);
                    }
                    else
                        return empty;

                case "remove":
                case "제거":
                    if (args.length == 2) {
                        ArrayList<String> list = new ArrayList<>();
                        list.add("new");
                        list.add("tool");
                        list.add("hat");
                        list.add("weapon");
                        list.add("etc");
                        return tabCompleteSort(list, args[1]);
                    }
                    else if (args.length == 3 && args[1].equals("new")) {
                        ArrayList<String> list = new ArrayList<>();
                        list.add("<index>");
                        return tabCompleteSort(list, args[2]);
                    }
                    else
                        return empty;

                default:
                    List<String> list_en = new ArrayList<>(Arrays.asList(en_commands));
                    return tabCompleteSort(list_en, args[0]);
            }
        } else {
            return empty;
        }
    }

    private List<String> tabCompleteSort(List<String> list, String args) {
        List<String> sortList = new ArrayList<>();
        for (String s : list) {
            if (args.isEmpty()) return list;

            if (s.toLowerCase().startsWith(args.toLowerCase()))
                sortList.add(s);
        }
        return sortList;
    }
}
