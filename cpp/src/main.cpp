#include <raylib.h>
#include <algorithm>
#include <array>
#include <cmath>
#include <map>
#include <random>
#include <string>
#include <vector>

enum class TileType { Grass, Sand, Water, IronOre, CopperOre, CoalOre, StoneOre, Tree };
enum class BuildingType { None, Chest, Furnace, Miner, Belt, Inserter, Assembler };

enum class Item {
    Wood, Stone, IronOre, CopperOre, Coal, IronPlate, CopperPlate,
    Gear, Wire, Circuit, FurnaceKit, ChestKit, BeltKit, InserterKit, MinerKit, AssemblerKit
};

struct Vec2i { int x; int y; };

struct Tile {
    TileType type = TileType::Grass;
    int resource = 0;
};

struct Inventory {
    std::map<Item, int> items;
    int get(Item it) const {
        auto f = items.find(it);
        return f == items.end() ? 0 : f->second;
    }
    bool has(Item it, int amount) const { return get(it) >= amount; }
    void add(Item it, int amount) { items[it] += amount; }
    bool remove(Item it, int amount) {
        if (!has(it, amount)) return false;
        items[it] -= amount;
        return true;
    }
};

struct Building {
    BuildingType type = BuildingType::None;
    Vec2i pos{};
    int dir = 1; // 0 up,1 right,2 down,3 left
    float work = 0.0f;
    Inventory inv;
};

struct Enemy {
    Vector2 pos{};
    Vector2 vel{};
    float hp = 25;
    float speed = 45;
};

struct Recipe {
    std::string name;
    std::vector<std::pair<Item,int>> in;
    std::pair<Item,int> out;
};

static std::string itemName(Item i) {
    switch(i){
        case Item::Wood: return "Wood"; case Item::Stone: return "Stone";
        case Item::IronOre: return "Iron Ore"; case Item::CopperOre: return "Copper Ore";
        case Item::Coal: return "Coal"; case Item::IronPlate: return "Iron Plate";
        case Item::CopperPlate: return "Copper Plate"; case Item::Gear: return "Gear";
        case Item::Wire: return "Wire"; case Item::Circuit: return "Circuit";
        case Item::FurnaceKit: return "Furnace"; case Item::ChestKit: return "Chest";
        case Item::BeltKit: return "Belt"; case Item::InserterKit: return "Inserter";
        case Item::MinerKit: return "Miner"; case Item::AssemblerKit: return "Assembler";
    }
    return "?";
}

static Color tileColor(TileType t){
    switch(t){
        case TileType::Grass: return Color{58,126,70,255};
        case TileType::Sand: return Color{194,174,115,255};
        case TileType::Water: return Color{50,98,180,255};
        case TileType::IronOre: return Color{103,114,130,255};
        case TileType::CopperOre: return Color{173,111,70,255};
        case TileType::CoalOre: return Color{55,55,65,255};
        case TileType::StoneOre: return Color{145,145,145,255};
        case TileType::Tree: return Color{40,90,40,255};
    }
    return WHITE;
}

struct Game {
    static constexpr int W = 128;
    static constexpr int H = 128;
    static constexpr int TILE = 24;

    std::array<std::array<Tile,W>,H> map{};
    std::array<std::array<BuildingType,W>,H> occupancy{};
    std::vector<Building> buildings;
    std::vector<Enemy> enemies;
    Inventory playerInv;
    Vector2 player{W*TILE/2.0f, H*TILE/2.0f};
    float playerHP = 100;
    float playerEnergy = 100;
    int selectedSlot = 0;
    int selectedBuildDir = 1;
    bool showCraft = false;
    bool showBuild = false;
    bool showGuide = false;
    bool showDebug = false;
    bool unlockedTier2 = false;
    bool unlockedTier3 = false;
    int nestsLeft = 4;
    long seed = 0;

    Camera2D cam{};

    Vec2i miningTile{-1,-1};
    float miningProgress = 0;

    std::vector<Recipe> recipes;

    float actionBufferMine = 0;
    float actionBufferPlace = 0;

    Game(long s):seed(s){
        initRecipes();
        generate();
        playerInv.add(Item::Wood, 12);
        playerInv.add(Item::Stone, 8);
        playerInv.add(Item::Coal, 6);
        cam.zoom = 1.0f;
    }

    void initRecipes(){
        recipes = {
            {"Iron Plate", {{Item::IronOre,1},{Item::Coal,1}}, {Item::IronPlate,1}},
            {"Copper Plate", {{Item::CopperOre,1},{Item::Coal,1}}, {Item::CopperPlate,1}},
            {"Gear", {{Item::IronPlate,2}}, {Item::Gear,1}},
            {"Wire", {{Item::CopperPlate,1}}, {Item::Wire,2}},
            {"Circuit", {{Item::Wire,2},{Item::IronPlate,1}}, {Item::Circuit,1}},
            {"Furnace", {{Item::Stone,8}}, {Item::FurnaceKit,1}},
            {"Chest", {{Item::Wood,6}}, {Item::ChestKit,1}},
            {"Belt", {{Item::IronPlate,1},{Item::Gear,1}}, {Item::BeltKit,2}},
            {"Inserter", {{Item::Gear,1},{Item::Wire,2},{Item::IronPlate,1}}, {Item::InserterKit,1}},
            {"Miner", {{Item::Gear,3},{Item::IronPlate,4}}, {Item::MinerKit,1}},
            {"Assembler", {{Item::Circuit,4},{Item::Gear,4},{Item::IronPlate,6}}, {Item::AssemblerKit,1}},
        };
    }

    void generate(){
        std::mt19937 rng((uint32_t)seed);
        std::uniform_real_distribution<float> d(0,1);
        for(int y=0;y<H;y++) for(int x=0;x<W;x++){
            float n = d(rng);
            map[y][x].type = n < 0.1f ? TileType::Water : (n < 0.2f ? TileType::Sand : TileType::Grass);
            map[y][x].resource = 0;
            occupancy[y][x] = BuildingType::None;
        }
        auto placePatch=[&](TileType t,int count,int radius,int amt){
            std::uniform_int_distribution<int> px(6,W-7), py(6,H-7);
            for(int i=0;i<count;i++){
                int cx=px(rng), cy=py(rng);
                for(int y=cy-radius;y<=cy+radius;y++) for(int x=cx-radius;x<=cx+radius;x++){
                    if(x<1||y<1||x>=W-1||y>=H-1) continue;
                    if((x-cx)*(x-cx)+(y-cy)*(y-cy) <= radius*radius && map[y][x].type!=TileType::Water){
                        map[y][x].type=t;
                        map[y][x].resource=amt;
                    }
                }
            }
        };
        placePatch(TileType::IronOre, 18, 4, 260);
        placePatch(TileType::CopperOre, 14, 4, 240);
        placePatch(TileType::CoalOre, 13, 3, 200);
        placePatch(TileType::StoneOre, 14, 3, 220);
        placePatch(TileType::Tree, 22, 4, 140);

        int sx=W/2, sy=H/2;
        for(int y=sy-8;y<=sy+8;y++) for(int x=sx-8;x<=sx+8;x++) if(x>=0&&y>=0&&x<W&&y<H && map[y][x].type==TileType::Water) map[y][x].type=TileType::Grass;

        std::uniform_int_distribution<int> ex(0,W-1), ey(0,H-1);
        for(int i=0;i<30;i++){
            Vector2 p{(float)(ex(rng)*TILE+TILE/2),(float)(ey(rng)*TILE+TILE/2)};
            if(Vector2Distance(p, player) > 420){
                enemies.push_back(Enemy{p,{0,0},30,42});
            }
        }
    }

    bool tileBlocked(int tx,int ty) const {
        if(tx<0||ty<0||tx>=W||ty>=H) return true;
        if(map[ty][tx].type==TileType::Water) return true;
        if(occupancy[ty][tx]!=BuildingType::None) return true;
        return false;
    }

    Vec2i worldToTile(Vector2 p) const {
        return {(int)floor(p.x/TILE),(int)floor(p.y/TILE)};
    }

    Vector2 tileCenter(int tx,int ty) const { return {(tx+0.5f)*TILE,(ty+0.5f)*TILE}; }

    void input(float dt){
        Vector2 dir{0,0};
        if(IsKeyDown(KEY_W)) dir.y -= 1;
        if(IsKeyDown(KEY_S)) dir.y += 1;
        if(IsKeyDown(KEY_A)) dir.x -= 1;
        if(IsKeyDown(KEY_D)) dir.x += 1;
        if(Vector2Length(dir)>0) dir = Vector2Scale(Vector2Normalize(dir), 150.0f*dt);

        Vector2 np = Vector2Add(player, dir);
        Vec2i t = worldToTile(np);
        if(!tileBlocked(t.x,t.y)) player = np;

        if(IsKeyPressed(KEY_C)) showCraft=!showCraft;
        if(IsKeyPressed(KEY_B)) showBuild=!showBuild;
        if(IsKeyPressed(KEY_G)) showGuide=!showGuide;
        if(IsKeyPressed(KEY_F3)) showDebug=!showDebug;
        if(IsKeyPressed(KEY_R)) playerEnergy = std::min(100.0f, playerEnergy+20.0f);

        if(IsKeyPressed(KEY_Q)) {
            playerHP -= 0; // reserved
        }

        for(int i=0;i<9;i++) if(IsKeyPressed(KEY_ONE+i)) selectedSlot=i;
        if(IsKeyPressed(KEY_LEFT)) selectedBuildDir=(selectedBuildDir+3)%4;
        if(IsKeyPressed(KEY_RIGHT)) selectedBuildDir=(selectedBuildDir+1)%4;

        actionBufferMine = std::max(0.0f, actionBufferMine-dt);
        actionBufferPlace = std::max(0.0f, actionBufferPlace-dt);

        if(IsMouseButtonPressed(MOUSE_LEFT_BUTTON)) actionBufferMine = 0.14f;
        if(IsMouseButtonPressed(MOUSE_RIGHT_BUTTON)) actionBufferPlace = 0.14f;

        if(showCraft) handleCraftPanel();
        if(showBuild) handleBuildPanel();

        mining(dt);

        if(actionBufferPlace > 0){
            actionBufferPlace = 0;
            placeSelected();
        }
    }

    void handleCraftPanel(){
        Rectangle panel{30,120,360,420};
        if(!CheckCollisionPointRec(GetMousePosition(), panel)) return;
        if(IsMouseButtonPressed(MOUSE_LEFT_BUTTON)){
            int idx = (GetMouseY()-140)/26;
            if(idx>=0 && idx < (int)recipes.size()) craft(recipes[idx]);
        }
    }

    void craft(const Recipe& r){
        for(auto &req:r.in) if(!playerInv.has(req.first, req.second)) return;
        for(auto &req:r.in) playerInv.remove(req.first, req.second);
        playerInv.add(r.out.first, r.out.second);
        if(playerInv.get(Item::MinerKit)>=1) unlockedTier2=true;
        if(playerInv.get(Item::AssemblerKit)>=1) unlockedTier3=true;
    }

    void mining(float dt){
        Vector2 mouseWorld = GetScreenToWorld2D(GetMousePosition(), cam);
        Vec2i t = worldToTile(mouseWorld);
        Vec2i pt = worldToTile(player);
        int dist = abs(t.x-pt.x)+abs(t.y-pt.y);
        bool mineHeld = IsMouseButtonDown(MOUSE_LEFT_BUTTON) || actionBufferMine > 0;

        if(!mineHeld || dist>2 || t.x<0||t.y<0||t.x>=W||t.y>=H){
            miningTile={-1,-1}; miningProgress=0; return;
        }

        Tile &tile = map[t.y][t.x];
        bool minable = tile.type==TileType::IronOre||tile.type==TileType::CopperOre||tile.type==TileType::CoalOre||tile.type==TileType::StoneOre||tile.type==TileType::Tree;
        if(!minable || tile.resource<=0){ miningProgress=0; miningTile={-1,-1}; return; }

        if(miningTile.x!=t.x || miningTile.y!=t.y){ miningTile=t; miningProgress=0; }
        miningProgress += dt*(tile.type==TileType::Tree?0.7f:1.0f);
        if(miningProgress >= 1.0f){
            miningProgress = 0;
            tile.resource -= 10;
            switch(tile.type){
                case TileType::IronOre: playerInv.add(Item::IronOre,1); break;
                case TileType::CopperOre: playerInv.add(Item::CopperOre,1); break;
                case TileType::CoalOre: playerInv.add(Item::Coal,1); break;
                case TileType::StoneOre: playerInv.add(Item::Stone,1); break;
                case TileType::Tree: playerInv.add(Item::Wood,1); break;
                default: break;
            }
            if(tile.resource<=0) tile.type = TileType::Grass;
            actionBufferMine = 0;
        }
    }

    void handleBuildPanel(){
        // UI-only; select slot 0..5 by clicking list.
        Rectangle panel{410,120,260,230};
        if(!CheckCollisionPointRec(GetMousePosition(), panel)) return;
        if(IsMouseButtonPressed(MOUSE_LEFT_BUTTON)){
            int idx=(GetMouseY()-140)/30;
            if(idx>=0 && idx<6) selectedSlot=idx;
        }
    }

    BuildingType slotToBuild() const {
        switch(selectedSlot){
            case 0: return BuildingType::Miner;
            case 1: return BuildingType::Furnace;
            case 2: return BuildingType::Chest;
            case 3: return BuildingType::Belt;
            case 4: return BuildingType::Inserter;
            case 5: return BuildingType::Assembler;
            default: return BuildingType::None;
        }
    }

    bool consumeBuildKit(BuildingType b){
        switch(b){
            case BuildingType::Miner: return playerInv.remove(Item::MinerKit,1);
            case BuildingType::Furnace: return playerInv.remove(Item::FurnaceKit,1);
            case BuildingType::Chest: return playerInv.remove(Item::ChestKit,1);
            case BuildingType::Belt: return playerInv.remove(Item::BeltKit,1);
            case BuildingType::Inserter: return playerInv.remove(Item::InserterKit,1);
            case BuildingType::Assembler: return playerInv.remove(Item::AssemblerKit,1);
            default: return false;
        }
    }

    void placeSelected(){
        BuildingType b = slotToBuild();
        if(b==BuildingType::None) return;
        Vector2 mouseWorld = GetScreenToWorld2D(GetMousePosition(), cam);
        Vec2i t = worldToTile(mouseWorld);
        if(t.x<0||t.y<0||t.x>=W||t.y>=H) return;
        if(occupancy[t.y][t.x]!=BuildingType::None) return;
        if(map[t.y][t.x].type==TileType::Water) return;
        if(Vector2Distance(tileCenter(t.x,t.y),player)<26) return;
        if(!consumeBuildKit(b)) return;

        occupancy[t.y][t.x]=b;
        Building built; built.type=b; built.pos=t; built.dir=selectedBuildDir;
        buildings.push_back(built);
    }

    void simulationTick(float dt){
        // survival drain
        playerEnergy = std::max(0.0f, playerEnergy - dt*1.5f);
        if(playerEnergy<=0) playerHP -= dt*3.0f;

        // automation
        for(auto &b:buildings){
            b.work += dt;
            Tile &tile = map[b.pos.y][b.pos.x];

            if(b.type==BuildingType::Miner && b.work>1.2f){
                b.work=0;
                if(tile.type==TileType::IronOre && tile.resource>0){ b.inv.add(Item::IronOre,1); tile.resource-=2; }
                if(tile.type==TileType::CopperOre && tile.resource>0){ b.inv.add(Item::CopperOre,1); tile.resource-=2; }
                if(tile.type==TileType::CoalOre && tile.resource>0){ b.inv.add(Item::Coal,1); tile.resource-=2; }
                if(tile.type==TileType::StoneOre && tile.resource>0){ b.inv.add(Item::Stone,1); tile.resource-=2; }
                if(tile.resource<=0 && (tile.type==TileType::IronOre||tile.type==TileType::CopperOre||tile.type==TileType::CoalOre||tile.type==TileType::StoneOre)) tile.type=TileType::Grass;
            }

            if(b.type==BuildingType::Furnace && b.work>1.6f){
                b.work=0;
                if(b.inv.remove(Item::IronOre,1) && b.inv.remove(Item::Coal,1)) b.inv.add(Item::IronPlate,1);
                else if(b.inv.remove(Item::CopperOre,1) && b.inv.remove(Item::Coal,1)) b.inv.add(Item::CopperPlate,1);
            }

            if(b.type==BuildingType::Assembler && b.work>2.0f){
                b.work=0;
                if(b.inv.remove(Item::Wire,2) && b.inv.remove(Item::IronPlate,1)) b.inv.add(Item::Circuit,1);
            }
        }

        // inserters pull+push
        for(auto &b:buildings){
            if(b.type!=BuildingType::Inserter || b.work<0.35f) continue;
            b.work=0;
            Vec2i from=b.pos, to=b.pos;
            if(b.dir==0){ from.y+=1; to.y-=1; }
            if(b.dir==1){ from.x-=1; to.x+=1; }
            if(b.dir==2){ from.y-=1; to.y+=1; }
            if(b.dir==3){ from.x+=1; to.x-=1; }
            Building* src = buildingAt(from.x,from.y);
            Building* dst = buildingAt(to.x,to.y);
            if(!src||!dst) continue;
            for(auto &it:src->inv.items){
                if(it.second>0){ it.second--; dst->inv.add(it.first,1); break; }
            }
        }

        // enemies
        updateEnemies(dt);
    }

    Building* buildingAt(int x,int y){
        for(auto &b:buildings) if(b.pos.x==x && b.pos.y==y) return &b;
        return nullptr;
    }

    void updateEnemies(float dt){
        for(auto &e:enemies){
            Vector2 toP = Vector2Subtract(player,e.pos);
            float d = Vector2Length(toP);
            Vector2 desired{0,0};
            if(d < 260){
                desired = Vector2Scale(Vector2Normalize(toP), e.speed);
            } else {
                desired.x = sinf(GetTime()+e.pos.x*0.01f)*20;
                desired.y = cosf(GetTime()+e.pos.y*0.01f)*20;
            }
            e.vel = Vector2Lerp(e.vel, desired, 0.08f);
            Vector2 next = Vector2Add(e.pos, Vector2Scale(e.vel, dt));
            Vec2i t = worldToTile(next);
            if(t.x>=0&&t.y>=0&&t.x<W&&t.y<H && map[t.y][t.x].type!=TileType::Water && occupancy[t.y][t.x]==BuildingType::None) e.pos = next;

            if(Vector2Distance(e.pos, player)<18){
                playerHP -= dt*8;
            }
        }

        if(IsMouseButtonPressed(MOUSE_LEFT_BUTTON)){
            Vector2 mw = GetScreenToWorld2D(GetMousePosition(), cam);
            for(auto &e:enemies){
                if(Vector2Distance(mw,e.pos)<14){
                    e.hp -= 20;
                }
            }
            enemies.erase(std::remove_if(enemies.begin(), enemies.end(), [&](const Enemy& e){return e.hp<=0;}), enemies.end());
        }
    }

    std::string objective() const {
        if(!unlockedTier2) return "Objective: Craft a Miner to unlock Tier 2 automation";
        if(!unlockedTier3) return "Objective: Craft an Assembler for Tier 3 production";
        if(!enemies.empty()) return "Objective: Clear nearby nests/enemies and expand";
        return "Objective complete: Area secured. Scale your factory.";
    }

    void draw(){
        cam.target = player;
        cam.offset = {(float)GetScreenWidth()/2 - 140, (float)GetScreenHeight()/2};

        BeginDrawing();
        ClearBackground(Color{14,16,22,255});

        BeginMode2D(cam);
        drawWorld();
        drawEntities();
        EndMode2D();

        drawHUD();
        if(showCraft) drawCraftPanel();
        if(showBuild) drawBuildPanel();
        if(showGuide) drawGuidePanel();
        if(showDebug) drawDebug();

        EndDrawing();
    }

    void drawWorld(){
        int sx = std::max(0, (int)((cam.target.x-cam.offset.x)/TILE)-2);
        int sy = std::max(0, (int)((cam.target.y-cam.offset.y)/TILE)-2);
        int ex = std::min(W-1, sx + GetScreenWidth()/TILE + 6);
        int ey = std::min(H-1, sy + GetScreenHeight()/TILE + 6);

        for(int y=sy;y<=ey;y++) for(int x=sx;x<=ex;x++){
            DrawRectangle(x*TILE, y*TILE, TILE, TILE, tileColor(map[y][x].type));
            DrawRectangleLines(x*TILE, y*TILE, TILE, TILE, Color{0,0,0,20});
            if(map[y][x].resource>0 && (map[y][x].type==TileType::IronOre||map[y][x].type==TileType::CopperOre||map[y][x].type==TileType::CoalOre||map[y][x].type==TileType::StoneOre||map[y][x].type==TileType::Tree)){
                DrawRectangle(x*TILE+6,y*TILE+6,12,12,Color{255,255,255,20});
            }
        }
    }

    void drawEntities(){
        for(auto &b:buildings){
            Color c{190,190,200,255};
            if(b.type==BuildingType::Miner) c={70,170,220,255};
            if(b.type==BuildingType::Furnace) c={220,130,80,255};
            if(b.type==BuildingType::Chest) c={150,105,58,255};
            if(b.type==BuildingType::Belt) c={90,90,95,255};
            if(b.type==BuildingType::Inserter) c={70,220,150,255};
            if(b.type==BuildingType::Assembler) c={170,110,220,255};
            DrawRectangle(b.pos.x*TILE+2,b.pos.y*TILE+2,TILE-4,TILE-4,c);
        }

        for(auto &e:enemies){
            DrawCircleV(e.pos, 10, Color{220,70,90,255});
            DrawCircleLines((int)e.pos.x,(int)e.pos.y,10,Color{255,255,255,40});
        }

        DrawCircleV(player, 10, Color{120,210,255,255});
        DrawCircleLines((int)player.x,(int)player.y,10,WHITE);

        if(miningTile.x>=0){
            Vector2 c = tileCenter(miningTile.x,miningTile.y);
            DrawRectangle((int)(c.x-12),(int)(c.y-20),24,4,Color{0,0,0,180});
            DrawRectangle((int)(c.x-12),(int)(c.y-20),(int)(24*miningProgress),4,Color{255,200,80,255});
        }
    }

    void drawHUD(){
        int x = GetScreenWidth()-340;
        DrawRectangle(x,0,340,GetScreenHeight(),Color{10,12,18,230});
        DrawText("AFTER THE FALL: FACTORY", x+16, 16, 24, Color{200,220,255,255});
        DrawText(TextFormat("HP: %.0f",playerHP), x+16, 58, 20, Color{255,120,120,255});
        DrawText(TextFormat("Energy: %.0f",playerEnergy), x+16, 84, 20, Color{255,220,120,255});
        DrawText(TextFormat("Enemies: %d",(int)enemies.size()), x+16, 110, 20, Color{220,220,220,255});
        DrawText(objective().c_str(), x+16, 138, 16, Color{170,230,180,255});

        DrawText("Hotbar (1-9)", x+16, 186, 18, SKYBLUE);
        const char* slots[6]={"1 Miner","2 Furnace","3 Chest","4 Belt","5 Inserter","6 Assembler"};
        for(int i=0;i<6;i++){
            Color c = i==selectedSlot?YELLOW:LIGHTGRAY;
            DrawText(slots[i], x+16, 212+i*22, 18, c);
        }

        DrawText("Inventory", x+16, 360, 18, SKYBLUE);
        int oy=386;
        std::vector<Item> shown={Item::Wood,Item::Stone,Item::IronOre,Item::CopperOre,Item::Coal,Item::IronPlate,Item::CopperPlate,Item::Gear,Item::Wire,Item::Circuit,
            Item::MinerKit,Item::FurnaceKit,Item::ChestKit,Item::BeltKit,Item::InserterKit,Item::AssemblerKit};
        for(auto it:shown){
            int c = playerInv.get(it);
            if(c>0){
                DrawText(TextFormat("%s: %d",itemName(it).c_str(),c), x+16, oy, 16, Color{210,210,210,255});
                oy += 18;
                if(oy>GetScreenHeight()-20) break;
            }
        }

        DrawText("C:Craft  B:Build  G:Guide  F3:Debug", x+16, GetScreenHeight()-26, 16, GRAY);
    }

    void drawCraftPanel(){
        DrawRectangle(30,120,360,420,Color{25,28,35,245});
        DrawText("Crafting", 46, 132, 24, SKYBLUE);
        for(int i=0;i<(int)recipes.size();i++){
            DrawText(TextFormat("%d) %s", i+1, recipes[i].name.c_str()), 46, 164+i*26, 18, RAYWHITE);
        }
    }

    void drawBuildPanel(){
        DrawRectangle(410,120,260,230,Color{25,28,35,245});
        DrawText("Build Menu", 424, 132, 24, SKYBLUE);
        DrawText("0 Miner", 424, 170, 20, RAYWHITE);
        DrawText("1 Furnace", 424, 200, 20, RAYWHITE);
        DrawText("2 Chest", 424, 230, 20, RAYWHITE);
        DrawText("3 Belt", 424, 260, 20, RAYWHITE);
        DrawText("4 Inserter", 424, 290, 20, RAYWHITE);
        DrawText("5 Assembler", 424, 320, 20, RAYWHITE);
    }

    void drawGuidePanel(){
        DrawRectangle(130,80,760,500,Color{18,22,28,245});
        DrawText("Guide / Progression", 150, 100, 30, Color{180,230,255,255});
        DrawText("1) Mine ore/wood: hold LMB on resource tiles.",150,160,22,RAYWHITE);
        DrawText("2) Open Craft (C): make Furnace/Chest first.",150,194,22,RAYWHITE);
        DrawText("3) Place buildings with RMB from hotbar 1-6.",150,228,22,RAYWHITE);
        DrawText("4) Feed ore+coal into Furnace -> plates.",150,262,22,RAYWHITE);
        DrawText("5) Craft Miner/Belt/Inserter for automation.",150,296,22,RAYWHITE);
        DrawText("6) Build Assembler for Tier 3 circuits.",150,330,22,RAYWHITE);
        DrawText("7) Defend/clear enemies while expanding.",150,364,22,RAYWHITE);
        DrawText("Automation rule: Inserter moves one item from source tile behind to destination tile ahead.",150,410,20,Color{255,220,160,255});
        DrawText("Press G to close",150,470,22,SKYBLUE);
    }

    void drawDebug(){
        DrawRectangle(14,14,300,120,Color{0,0,0,170});
        Vec2i t = worldToTile(player);
        DrawText(TextFormat("Seed: %lld",seed),24,24,18,LIME);
        DrawText(TextFormat("FPS: %d",GetFPS()),24,46,18,LIME);
        DrawText(TextFormat("Player tile: %d,%d",t.x,t.y),24,68,18,LIME);
        DrawText(TextFormat("Enemies: %d  Buildings: %d",(int)enemies.size(),(int)buildings.size()),24,90,18,LIME);
        DrawText(TextFormat("Selected: %d",selectedSlot),24,112,18,LIME);
    }
};

int main(int argc,char** argv){
    long seed = (long)GetTime();
    for(int i=1;i<argc;i++){
        std::string a=argv[i];
        if(a=="--seed" && i+1<argc) seed = std::stol(argv[++i]);
    }

    SetConfigFlags(FLAG_WINDOW_RESIZABLE | FLAG_VSYNC_HINT);
    InitWindow(1360, 860, "After The Fall - Factory Survival");
    SetTargetFPS(60);

    Game game(seed==0?12345:seed);

    float simAccumulator = 0;
    const float simStep = 1.0f/20.0f;

    while(!WindowShouldClose()){
        float dt = GetFrameTime();
        game.input(dt);

        simAccumulator += dt;
        while(simAccumulator >= simStep){
            game.simulationTick(simStep);
            simAccumulator -= simStep;
        }

        game.draw();
    }

    CloseWindow();
    return 0;
}
