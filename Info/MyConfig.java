package Info;

public class MyConfig {
    public int width = 40;
    public int height = 30;
    public int food_static = 1;
    public float food_per_player = 1;
    public int state_delay_ms = 1000;
    public float dead_food_prob = 1 ;
    public int ping_delay_ms = 100;
    public int node_timeout_ms = 800;

    public void print(){
        System.out.println(width+"*"+height+"\n"+food_static+"\n"+food_per_player+"\n"+state_delay_ms+"\n"+dead_food_prob+"\n"+ping_delay_ms+"\n"+node_timeout_ms);
    }
}
