package com.wsc.game;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import java.util.*;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        hideUI();
        SaveManager save = new SaveManager(this);
        GameView game = new GameView(this, save);
        FrameLayout root = new FrameLayout(this);
        root.addView(game, new FrameLayout.LayoutParams(-1, -1));
        root.addView(game.getModMenu(), new FrameLayout.LayoutParams(-1, -1));
        setContentView(root);
    }

    private void hideUI() {
        View d = getWindow().getDecorView();
        d.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          | View.SYSTEM_UI_FLAG_FULLSCREEN
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    @Override
    public void onWindowFocusChanged(boolean h) {
        super.onWindowFocusChanged(h);
        if (h) hideUI();
    }

    static class SaveManager {
        private SharedPreferences p;
        SaveManager(Context c) {
            p = c.getSharedPreferences("wsc_save", Context.MODE_PRIVATE);
            if (!p.getBoolean("gift_v3", false)) {
                p.edit()
                    .putInt("coins", 50000)
                    .putInt("medkits", 25)
                    .putInt("energy", 999)
                    .putBoolean("skip", true)
                    .putBoolean("noads", true)
                    .putBoolean("gift_v3", true)
                    .apply();
            }
        }
        int getCoins() { return p.getInt("coins", 0); }
        void setCoins(int v) { p.edit().putInt("coins", v).apply(); }
        void addCoins(int v) { setCoins(getCoins() + v); }
        int getMedkits() { return p.getInt("medkits", 0); }
        void setMedkits(int v) { p.edit().putInt("medkits", v).apply(); }
        void addMedkits(int v) { setMedkits(getMedkits() + v); }
        int getEnergy() { return p.getInt("energy", 0); }
        void setEnergy(int v) { p.edit().putInt("energy", v).apply(); }
        void addEnergy(int v) { setEnergy(getEnergy() + v); }
        boolean canSkip() { return p.getBoolean("skip", true); }
        boolean noAds() { return p.getBoolean("noads", true); }
        int getWins() { return p.getInt("wins", 0); }
        int getLosses() { return p.getInt("losses", 0); }
        int getDraws() { return p.getInt("draws", 0); }
        void addWin() { p.edit().putInt("wins", getWins() + 1).apply(); }
        void addLoss() { p.edit().putInt("losses", getLosses() + 1).apply(); }
        void addDraw() { p.edit().putInt("draws", getDraws() + 1).apply(); }
    }
  static class Player {
    String name; int pos; int ov;
    Player(String n, int p, int o) { name = n; pos = p; ov = o; }
}

static class Team {
    String name;
    int c1, c2, c3;
    int atk, def, mid;
    List<Player> squad = new ArrayList<>();
    Team(String n, int a, int b, int c, int x, int y, int z) {
        name = n; c1 = a; c2 = b; c3 = c; atk = x; def = y; mid = z;
        String[] f = {"Léo","Rafa","Caio","Bruno","Diego","Vini","Gabriel","Mateus","Thiago","Lucas","Pedro"};
        String[] l = {"Silva","Souza","Costa","Alves","Rocha","Lima","Moura","Pires","Nunes","Dias"};
        int[] ps = {0,1,1,1,1,2,2,2,3,3,3};
        for (int i = 0; i < 11; i++) {
            int base = ps[i] == 0 ? def : (ps[i] == 1 ? def : (ps[i] == 2 ? mid : atk));
            squad.add(new Player(f[(i*3)%f.length] + " " + l[(i*5)%l.length], ps[i], base - 3 + (i % 4)));
        }
    }
    static List<Team> all() {
        List<Team> t = new ArrayList<>();
        t.add(new Team("Flamingos",    0xFFE30613, 0xFF000000, 0xFFFFFFFF, 88, 84, 86));
        t.add(new Team("Palmeras",     0xFF006437, 0xFFFFFFFF, 0xFF1B5E20, 85, 82, 83));
        t.add(new Team("Corintias",    0xFF000000, 0xFFFFFFFF, 0xFFCCCCCC, 83, 80, 81));
        t.add(new Team("Sao Paulino",  0xFFCC0000, 0xFFFFFFFF, 0xFF000000, 82, 84, 82));
        t.add(new Team("Santos FC",    0xFFFFFFFF, 0xFF000000, 0xFFCCCCCC, 80, 78, 81));
        t.add(new Team("Vasco da Grã", 0xFF000000, 0xFFFFFFFF, 0xFFCC0000, 78, 79, 77));
        t.add(new Team("Bota Fogo",    0xFF000000, 0xFFFFFFFF, 0xFF888888, 79, 77, 78));
        t.add(new Team("Fluminese",    0xFF7A1E2C, 0xFF006437, 0xFFFFFFFF, 81, 80, 80));
        t.add(new Team("Grêmio Real",  0xFF1565C0, 0xFF000000, 0xFFFFFFFF, 80, 82, 80));
        t.add(new Team("Internacional",0xFFCC0000, 0xFFFFFFFF, 0xFF000000, 82, 81, 82));
        t.add(new Team("Cruzerio",     0xFF1565C0, 0xFFFFFFFF, 0xFF000000, 81, 80, 81));
        t.add(new Team("Atletico MG",  0xFF000000, 0xFFFFFFFF, 0xFFCCCCCC, 84, 82, 83));
        t.add(new Team("Bahía",        0xFF1565C0, 0xFFCC0000, 0xFFFFFFFF, 79, 78, 78));
        t.add(new Team("Fortaleza EC", 0xFF1565C0, 0xFFCC0000, 0xFFFFFFFF, 78, 79, 78));
        t.add(new Team("Real Madri",   0xFFFFFFFF, 0xFFD4AF37, 0xFF000080, 92, 90, 91));
        t.add(new Team("Barcelonas",   0xFFA50044, 0xFF004D98, 0xFFEDBB00, 91, 89, 92));
        t.add(new Team("Manch. City",  0xFF6CABDD, 0xFFFFFFFF, 0xFF1C2C5B, 93, 91, 93));
        t.add(new Team("Liverpol",     0xFFC8102E, 0xFFFFFFFF, 0xFF00B2A9, 90, 89, 90));
        t.add(new Team("Baviera",      0xFFDC052D, 0xFFFFFFFF, 0xFF0066B2, 92, 90, 91));
        t.add(new Team("Paris SG",     0xFF004170, 0xFFFF0000, 0xFFFFFFFF, 91, 89, 90));
        return t;
    }
}
  static class MatchEngine {
    static class Result {
        int hg, ag;
        List<String> events = new ArrayList<>();
    }
    static Result play(Team h, Team a) {
        Result r = new Result();
        Random rnd = new Random();
        double hp = (h.atk*0.5 + h.mid*0.3 + h.def*0.2) * 1.05;
        double ap = a.atk*0.5 + a.mid*0.3 + a.def*0.2;
        double tot = hp + ap;
        r.hg = goals(rnd, 3.0 * (hp/tot));
        r.ag = goals(rnd, 3.0 * (ap/tot));
        int tg = r.hg + r.ag;
        for (int i = 0; i < tg; i++) {
            int min = 1 + rnd.nextInt(90);
            boolean home = rnd.nextDouble() < (double) r.hg / Math.max(1, tg);
            Team t = home ? h : a;
            List<Player> atk = new ArrayList<>();
            for (Player pl : t.squad) if (pl.pos >= 2) atk.add(pl);
            if (atk.isEmpty()) atk = t.squad;
            String sc = atk.get(rnd.nextInt(atk.size())).name;
            r.events.add(min + "' " + sc + " (" + t.name + ")");
        }
        Collections.sort(r.events, (x, y) -> {
            int mx = Integer.parseInt(x.split("'")[0]);
            int my = Integer.parseInt(y.split("'")[0]);
            return Integer.compare(mx, my);
        });
        return r;
    }
    static int goals(Random r, double e) {
        double v = r.nextDouble();
        if (e < 0.8) return v < 0.55 ? 0 : (v < 0.85 ? 1 : 2);
        if (e < 1.4) return v < 0.30 ? 0 : (v < 0.70 ? 1 : (v < 0.92 ? 2 : 3));
        if (e < 2.0) return v < 0.15 ? 0 : (v < 0.50 ? 1 : (v < 0.85 ? 2 : 3));
        return v < 0.08 ? 0 : (v < 0.35 ? 1 : (v < 0.70 ? 2 : (v < 0.92 ? 3 : 4)));
    }
  }
  static class SpriteFactory {
    static Bitmap crest(Team t, int s) {
        Bitmap b = Bitmap.createBitmap(s, s, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(t.c1);
        c.drawCircle(s/2f, s/2f, s/2f - 2, p);
        p.setColor(t.c2);
        Path path = new Path();
        path.moveTo(0, s*0.65f);
        path.lineTo(s, s*0.35f);
        path.lineTo(s, s*0.55f);
        path.lineTo(0, s*0.85f);
        path.close();
        c.drawPath(path, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(6);
        p.setColor(t.c3);
        c.drawCircle(s/2f, s/2f, s/2f - 4, p);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        Path star = new Path();
        float cx = s/2f, cy = s/2f, rO = s*0.22f, rI = s*0.10f;
        double ang = -Math.PI/2, step = Math.PI/5;
        for (int i = 0; i < 10; i++) {
            float r = (i % 2 == 0) ? rO : rI;
            float x = (float)(cx + Math.cos(ang)*r);
            float y = (float)(cy + Math.sin(ang)*r);
            if (i == 0) star.moveTo(x, y); else star.lineTo(x, y);
            ang += step;
        }
        star.close();
        c.drawPath(star, p);
        return b;
    }
  }
  static class ModMenu extends View {
    boolean open = false;
    SaveManager save;
    Paint bg, txt, txtT, btn, btnP, btnT;
    RectF bCoins = new RectF(), bMed = new RectF(), bEn = new RectF(),
          bMax = new RectF(), bClose = new RectF(), bTog = new RectF();
    boolean pC, pM, pE, pX, pCl;

    ModMenu(Context c, SaveManager s) {
        super(c);
        save = s;
        bg = new Paint(Paint.ANTI_ALIAS_FLAG); bg.setColor(0xEE0F172A);
        txt = new Paint(Paint.ANTI_ALIAS_FLAG); txt.setColor(Color.WHITE); txt.setTextSize(30);
        txtT = new Paint(Paint.ANTI_ALIAS_FLAG); txtT.setColor(0xFF00E5FF); txtT.setTextSize(46); txtT.setFakeBoldText(true);
        btn = new Paint(Paint.ANTI_ALIAS_FLAG); btn.setColor(0xFF1E88E5);
        btnP = new Paint(Paint.ANTI_ALIAS_FLAG); btnP.setColor(0xFF0D47A1);
        btnT = new Paint(Paint.ANTI_ALIAS_FLAG); btnT.setColor(0xFFD81B60);
    }

    @Override
    protected void onDraw(Canvas c) {
        int w = getWidth(), h = getHeight();
        bTog.set(w - 180, 30, w - 30, 130);
        c.drawRoundRect(bTog, 24, 24, open ? btnP : btnT);
        txt.setColor(Color.WHITE); txt.setTextSize(42); txt.setTextAlign(Paint.Align.CENTER);
        c.drawText("MOD", bTog.centerX(), bTog.centerY() + 14, txt);
        if (!open) return;

        Paint s = new Paint(); s.setColor(0x99000000);
        c.drawRect(0, 0, w, h, s);

        float pw = Math.min(w - 100, 900), ph = Math.min(h - 80, 700);
        float px = (w - pw)/2f, py = (h - ph)/2f;
        c.drawRoundRect(new RectF(px, py, px+pw, py+ph), 30, 30, bg);

        txtT.setTextAlign(Paint.Align.LEFT);
        c.drawText("MOD MENU", px + 50, py + 80, txtT);

        float bw = pw - 100, bh = 85, bx = px + 50, by = py + 130, g = 18;
        txt.setTextSize(30);

        bCoins.set(bx, by, bx+bw, by+bh);
        c.drawRoundRect(bCoins, 16, 16, pC ? btnP : btn);
        txt.setColor(Color.WHITE); txt.setTextAlign(Paint.Align.LEFT);
        c.drawText("+10.000 Moedas  (atual: " + save.getCoins() + ")", bx + 25, by + 55, txt);

        by += bh + g;
        bMed.set(bx, by, bx+bw, by+bh);
        c.drawRoundRect(bMed, 16, 16, pM ? btnP : btn);
        c.drawText("+10 Kits Médicos  (atual: " + save.getMedkits() + ")", bx + 25, by + 55, txt);

        by += bh + g;
        bEn.set(bx, by, bx+bw, by+bh);
        c.drawRoundRect(bEn, 16, 16, pE ? btnP : btn);
        c.drawText("+100 Energia  (atual: " + save.getEnergy() + ")", bx + 25, by + 55, txt);

        by += bh + g;
        bMax.set(bx, by, bx+bw, by+bh);
        c.drawRoundRect(bMax, 16, 16, pX ? btnP : btn);
        c.drawText("MAX TUDO (999999 / 999 / 9999)", bx + 25, by + 55, txt);

        bClose.set(px + pw - 220, py + ph - 95, px + pw - 50, py + ph - 30);
        c.drawRoundRect(bClose, 16, 16, pCl ? btnP : btnT);
        txt.setTextAlign(Paint.Align.CENTER);
        c.drawText("FECHAR", bClose.centerX(), bClose.centerY() + 12, txt);

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX(), y = e.getY();
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (bTog.contains(x, y)) { open = !open; invalidate(); return true; }
            if (!open) return false;
            if (bCoins.contains(x, y)) { save.addCoins(10000); pC = true; invalidate(); return true; }
            if (bMed.contains(x, y)) { save.addMedkits(10); pM = true; invalidate(); return true; }
            if (bEn.contains(x, y)) { save.addEnergy(100); pE = true; invalidate(); return true; }
            if (bMax.contains(x, y)) {
                save.setCoins(999999); save.setMedkits(999); save.setEnergy(9999);
                pX = true; invalidate(); return true;
            }
            if (bClose.contains(x, y)) { open = false; pCl = true; invalidate(); return true; }
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            pC = pM = pE = pX = pCl = false;
            invalidate();
        }
        return true;
    }
    }
  static class GameView extends View {
    enum Screen { MENU, SELECT, MATCH, RESULT }
    SaveManager save;
    ModMenu mod;
    List<Team> teams = Team.all();
    Team mine = teams.get(0), opp = teams.get(14);
    Bitmap crestMine, crestOpp;
    Screen screen = Screen.MENU;
    MatchEngine.Result last;
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint txt = new Paint(Paint.ANTI_ALIAS_FLAG);
    RectF bPlay = new RectF(), bChange = new RectF(), bBack = new RectF(), bMenu = new RectF();

    GameView(Context c, SaveManager s) {
        super(c);
        save = s;
        mod = new ModMenu(c, s);
        refresh();
    }

    ModMenu getModMenu() { return mod; }

    void refresh() {
        crestMine = SpriteFactory.crest(mine, 200);
        crestOpp = SpriteFactory.crest(opp, 200);
    }

    @Override
    protected void onDraw(Canvas c) {
        int w = getWidth(), h = getHeight();
        LinearGradient lg = new LinearGradient(0, 0, 0, h, 0xFF0B6E2C, 0xFF03361A, Shader.TileMode.CLAMP);
        p.setShader(lg);
        c.drawRect(0, 0, w, h, p);
        p.setShader(null);
        p.setColor(0x33FFFFFF); p.setStrokeWidth(4);
        c.drawLine(w/2f, 0, w/2f, h, p);
        c.drawCircle(w/2f, h/2f, 150, p);
        c.drawRect(20, h*0.15f, w - 20, h*0.85f, p);

        switch (screen) {
            case MENU: drawMenu(c, w, h); break;
            case SELECT: drawSelect(c, w, h); break;
            case MATCH: drawMatch(c, w, h); break;
            case RESULT: drawResult(c, w, h); break;
        }

        mod.draw(c);
        invalidate();
    }

    void drawMenu(Canvas c, int w, int h) {
        txt.setColor(Color.WHITE); txt.setTextAlign(Paint.Align.CENTER);
        txt.setFakeBoldText(true);
        txt.setTextSize(Math.min(w * 0.06f, 80));
        c.drawText("WORLD SOCCER CHAMPS", w/2f, h * 0.16f, txt);
        txt.setFakeBoldText(false); txt.setTextSize(28); txt.setColor(0xFFCCFFCC);
        c.drawText("Clone MOD — sem licença", w/2f, h * 0.21f, txt);

        c.drawBitmap(crestMine, w/2f - 100, h * 0.25f, null);
        txt.setColor(Color.WHITE); txt.setFakeBoldText(true); txt.setTextSize(42);
        c.drawText(mine.name, w/2f, h * 0.25f + 240, txt);

        txt.setFakeBoldText(false); txt.setTextSize(28); txt.setTextAlign(Paint.Align.LEFT);
        float sx = w * 0.07f, sy = h * 0.72f;
        c.drawText("Moedas: " + save.getCoins(), sx, sy, txt);
        c.drawText("Kits: " + save.getMedkits(), sx + w*0.32f, sy, txt);
        c.drawText("Energia: " + save.getEnergy(), sx + w*0.62f, sy, txt);
        c.drawText("V: " + save.getWins() + "  E: " + save.getDraws() + "  D: " + save.getLosses(), sx, sy + 45, txt);
        c.drawText("Pular jogos: ATIVO    Sem anúncios: ATIVO", sx, sy + 90, txt);

        float bw = 380, bh = 100;
        bPlay.set(w * 0.07f, h * 0.88f, w * 0.07f + bw, h * 0.88f + bh);
        bChange.set(w - 0.07f*w - bw, h * 0.88f, w - 0.07f*w, h * 0.88f + bh);
        btn(c, bPlay, "JOGAR", 0xFF00C853);
        btn(c, bChange, "TROCAR TIME", 0xFF1E88E5);
    }
            void drawSelect(Canvas c, int w, int h) {
            txt.setColor(Color.WHITE); txt.setFakeBoldText(true);
            txt.setTextSize(48); txt.setTextAlign(Paint.Align.CENTER);
            c.drawText("ESCOLHA SEU TIME", w/2f, 80, txt);

            int cols = 4;
            float padX = 40, padY = 110, cw = (w - padX*2)/cols, ch = (h - padY - 130) / 5f;
            txt.setTextSize(24); txt.setFakeBoldText(true);
            for (int i = 0; i < teams.size(); i++) {
                Team t = teams.get(i);
                int col = i % cols, row = i / cols;
                float x = padX + col*cw, y = padY + row*ch;
                p.setColor(t.c1);
                c.drawRoundRect(new RectF(x+6, y+6, x+cw-6, y+ch-6), 14, 14, p);
                p.setColor(t.c2);
                c.drawRoundRect(new RectF(x+6, y+ch-26, x+cw-6, y+ch-6), 14, 14, p);
                Bitmap cr = SpriteFactory.crest(t, 80);
                c.drawBitmap(cr, x + cw/2f - 40, y + 15, null);
                txt.setColor(Color.WHITE);
                c.drawText(t.name, x + cw/2f, y + ch - 38, txt);
            }
            bBack.set(30, h - 100, 240, h - 25);
            btn(c, bBack, "VOLTAR", 0xFF757575);
        }

        void drawMatch(Canvas c, int w, int h) {
            txt.setColor(Color.WHITE); txt.setFakeBoldText(true);
            txt.setTextAlign(Paint.Align.CENTER);
            p.setColor(0xCC000000);
            c.drawRoundRect(new RectF(w*0.15f, 50, w*0.85f, 240), 24, 24, p);
            c.drawBitmap(crestMine, w*0.20f, 80, null);
            c.drawBitmap(crestOpp, w*0.78f - 100, 80, null);
            if (last != null) {
                txt.setTextSize(80);
                c.drawText(last.hg + "  x  " + last.ag, w/2f, 170, txt);
                txt.setTextSize(26);
                c.drawText(mine.name + "      vs      " + opp.name, w/2f, 220, txt);
            }
            txt.setTextAlign(Paint.Align.LEFT); txt.setTextSize(26);
            float ey = 300;
            if (last != null) {
                int cnt = 0;
                for (String ev : last.events) {
                    c.drawText(ev, 70, ey, txt);
                    ey += 38;
                    if (++cnt >= 10) break;
                }
            }
            bMenu.set(w - 350, h - 100, w - 30, h - 25);
            btn(c, bMenu, "MENU", 0xFF757575);
        }

        void drawResult(Canvas c, int w, int h) {
            txt.setTextAlign(Paint.Align.CENTER); txt.setFakeBoldText(true);
            txt.setColor(Color.WHITE); txt.setTextSize(64);
            c.drawText("FIM DE JOGO", w/2f, h * 0.25f, txt);
            if (last != null) {
                txt.setTextSize(130);
                c.drawText(last.hg + " x " + last.ag, w/2f, h * 0.55f, txt);
                String msg = last.hg > last.ag ? "VITORIA!" : (last.hg < last.ag ? "DERROTA" : "EMPATE");
                txt.setTextSize(52);
                c.drawText(msg, w/2f, h * 0.72f, txt);
            }
            bBack.set(w/2f - 220, h - 120, w/2f + 220, h - 35);
            btn(c, bBack, "VOLTAR AO MENU", 0xFF1E88E5);
        }

        void btn(Canvas c, RectF r, String label, int color) {
            p.setColor(color);
            c.drawRoundRect(r, 20, 20, p);
            txt.setColor(Color.WHITE); txt.setTextAlign(Paint.Align.CENTER);
            txt.setFakeBoldText(true);
            txt.setTextSize(Math.min(42, r.height() * 0.42f));
            c.drawText(label, r.centerX(), r.centerY() + r.height() * 0.14f, txt);
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            if (mod.onTouchEvent(e)) return true;
            if (e.getAction() != MotionEvent.ACTION_UP) return true;
            float x = e.getX(), y = e.getY();

            switch (screen) {
                case MENU:
                    if (bPlay.contains(x, y)) {
                        save.addEnergy(-10);
                        List<Team> pool = new ArrayList<>(teams);
                        pool.remove(mine);
                        opp = pool.get((int)(Math.random() * pool.size()));
                        refresh();
                        last = MatchEngine.play(mine, opp);
                        if (last.hg > last.ag) save.addWin();
                        else if (last.hg < last.ag) save.addLoss();
                        else save.addDraw();
                        save.addCoins(500);
                        screen = Screen.RESULT;
                    } else if (bChange.contains(x, y)) {
                        screen = Screen.SELECT;
                    }
                    return true;

                case SELECT:
                    if (bBack.contains(x, y)) { screen = Screen.MENU; return true; }
                    int cols = 4;
                    float padX = 40, padY = 110;
                    float cw = (getWidth() - padX*2) / cols;
                    float ch = (getHeight() - padY - 130) / 5f;
                    for (int i = 0; i < teams.size(); i++) {
                        int col = i % cols, row = i / cols;
                        float cx = padX + col*cw, cy = padY + row*ch;
                        if (x >= cx && x <= cx+cw && y >= cy && y <= cy+ch) {
                            mine = teams.get(i);
                            refresh();
                            screen = Screen.MENU;
                            return true;
                        }
                    }
                    return true;

                case MATCH:
                    if (bMenu.contains(x, y)) screen = Screen.MENU;
                    return true;

                case RESULT:
                    if (bBack.contains(x, y)) screen = Screen.MENU;
                    return true;
            }
            return true;
        }
    }
                           }
