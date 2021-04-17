package xyz.zedler.patrick.grocy.util;

/*
    This file is part of Grocy Android.

    Grocy Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Grocy Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Grocy Android.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2020-2021 by Patrick Zedler & Dominic Zedler
*/

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.zedler.patrick.grocy.R;
import xyz.zedler.patrick.grocy.activity.MainActivity;

public class ShortcutUtil {

    public final static String SHOPPING_LIST = "shortcut_shopping_list";
    public final static String ADD_TO_SHOPPING_LIST = "shortcut_add_to_shopping_list";
    public final static String SHOPPING_MODE = "shortcut_shopping_mode";
    public final static String PURCHASE = "shortcut_purchase";
    public final static String CONSUME = "shortcut_consume";

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static List<ShortcutInfo> getDynamicShortcuts(Context context) {
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
        return shortcutManager.getDynamicShortcuts();
    }

    public static void refreshShortcuts(Context context, Uri uriAddToShoppingListDeepLink) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N_MR1) return;
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
        List<ShortcutInfo> shortcutInfos = shortcutManager.getDynamicShortcuts();
        List<ShortcutInfo> newShortcutInfos = new ArrayList<>();
        for(ShortcutInfo shortcutInfo : shortcutInfos) {
            if(shortcutInfo.getId().equals(SHOPPING_LIST)) {
                newShortcutInfos.add(createShortcutShoppingList(
                        context, context.getString(R.string.title_shopping_list)
                ));
            } else if(shortcutInfo.getId().equals(ADD_TO_SHOPPING_LIST)) {
                newShortcutInfos.add(createShortcutAddToShoppingList(
                        context, uriAddToShoppingListDeepLink,
                        context.getString(R.string.action_shopping_list_add)
                ));
            } else if(shortcutInfo.getId().equals(SHOPPING_MODE)) {
                newShortcutInfos.add(createShortcutShoppingMode(
                        context, context.getString(R.string.title_shopping_mode)
                ));
            } else if(shortcutInfo.getId().equals(PURCHASE)) {
                newShortcutInfos.add(createShortcutPurchase(
                        context, context.getString(R.string.title_purchase)
                ));
            } else if(shortcutInfo.getId().equals(CONSUME)) {
                newShortcutInfos.add(createShortcutConsume(
                        context, context.getString(R.string.title_consume)
                ));
            }
        }
        List<String> shortcutIdsSorted = Arrays.asList(
                SHOPPING_LIST, ADD_TO_SHOPPING_LIST,
                SHOPPING_MODE, PURCHASE, CONSUME
        );
        shortcutManager.removeAllDynamicShortcuts();
        newShortcutInfos = SortUtil.sortShortcutsById(newShortcutInfos, shortcutIdsSorted);
        shortcutManager.setDynamicShortcuts(newShortcutInfos);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static ShortcutInfo createShortcutShoppingList(Context context, CharSequence label) {
        Uri uri = Uri.parse(context.getString(R.string.deep_link_shoppingListFragment));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setClass(context, MainActivity.class);
        return new ShortcutInfo.Builder(context, SHOPPING_LIST)
                .setShortLabel(label)
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_shopping_list))
                .setIntent(intent).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static ShortcutInfo createShortcutAddToShoppingList(Context context, Uri uri, CharSequence label) {
        Intent intent = getIntent(context, uri);
        return new ShortcutInfo.Builder(context, ADD_TO_SHOPPING_LIST)
                .setShortLabel(label)
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_add))
                .setIntent(intent).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static ShortcutInfo createShortcutShoppingMode(Context context, CharSequence label) {
        Uri uri = Uri.parse(context.getString(R.string.deep_link_shoppingModeFragment));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setClass(context, MainActivity.class);
        return new ShortcutInfo.Builder(context, SHOPPING_MODE)
                .setShortLabel(label)
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_shopping_mode))
                .setIntent(intent).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static ShortcutInfo createShortcutPurchase(Context context, CharSequence label) {
        Uri uri = Uri.parse(context.getString(R.string.deep_link_purchaseFragment));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setClass(context, MainActivity.class);
        return new ShortcutInfo.Builder(context, PURCHASE)
                .setShortLabel(label)
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_purchase))
                .setIntent(intent).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static ShortcutInfo createShortcutConsume(Context context, CharSequence label) {
        Uri uri = Uri.parse(context.getString(R.string.deep_link_consumeFragment));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setClass(context, MainActivity.class);
        return new ShortcutInfo.Builder(context, CONSUME)
                .setShortLabel(label)
                .setIcon(Icon.createWithResource(context, R.mipmap.ic_consume))
                .setIntent(intent).build();
    }

    private static Intent getIntent(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setClass(context, MainActivity.class);
        return intent;
    }
}