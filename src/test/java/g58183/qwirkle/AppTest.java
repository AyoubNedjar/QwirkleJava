package g58183.qwirkle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

@Test
    void firstTest(){
    String command = "f u 2 4";
    App.playQwirkleCommand(command);
}

}