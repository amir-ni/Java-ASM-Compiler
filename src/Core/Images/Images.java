package Core.Images;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Images {
    public static int if_width = 239;
    public static int id_width = 255;
    public static int ex_width = 212;
    public static int mem_width = 166;
    public static int wb_width = 128;
    public static int height = 600;

    public static Image if_stage;
    public static Image if2_stage;

    public static Image id_stage;
    public static Image id_wb;
    public static Image id_beq_lw;
    public static Image id_beq_lw_wb;
    public static Image id_r;
    public static Image id_r_wb;
    public static Image id_sw;
    public static Image id_sw_wb;

    public static Image ex_stage;
    public static Image ex_beq;
    public static Image ex_sw;
    public static Image ex_r;

    public static Image mem_stage;
    public static Image mem_beq;
    public static Image mem_lw;
    public static Image mem_sw;
    public static Image mem_r;

    public static Image wb_stage;
    public static Image wb_;
    public static Image wb_write;

    public static Image previousIcon;
    public static Image nextIcon;
    public static Image applyIcon;


    static {
        try {
            if_stage = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/if/if.png")).
                    getScaledInstance(if_width, height, Image.SCALE_SMOOTH);
            if2_stage = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/if/if_2.png")).
                    getScaledInstance(if_width, height, Image.SCALE_SMOOTH);
            id_stage = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/id/id.png")).
                    getScaledInstance(id_width, height, Image.SCALE_SMOOTH);
            id_beq_lw = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/id/id_beq_lw.png")).
                    getScaledInstance(id_width, height, Image.SCALE_SMOOTH);
            id_beq_lw_wb = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/id/id_beq_lw_wb.png")).
                    getScaledInstance(id_width, height, Image.SCALE_SMOOTH);
            id_r = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/id/id_r-type.png")).
                    getScaledInstance(id_width, height, Image.SCALE_SMOOTH);
            id_r_wb = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/id/id_r-type_wb.png")).
                    getScaledInstance(id_width, height, Image.SCALE_SMOOTH);
            id_sw = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/id/id_sw.png")).
                    getScaledInstance(id_width, height, Image.SCALE_SMOOTH);
            id_sw_wb = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/id/id_sw_wb.png")).
                    getScaledInstance(id_width, height, Image.SCALE_SMOOTH);
            id_wb = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/id/id_white_wb.png")).
                    getScaledInstance(id_width, height, Image.SCALE_SMOOTH);
            ex_stage = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/ex/ex.png")).
                    getScaledInstance(ex_width, height, Image.SCALE_SMOOTH);
            ex_beq = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/ex/beq.png")).
                    getScaledInstance(ex_width, height, Image.SCALE_SMOOTH);
            ex_r = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/ex/r-type.png")).
                    getScaledInstance(ex_width, height, Image.SCALE_SMOOTH);
            ex_sw = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/ex/sw.png")).
                    getScaledInstance(ex_width, height, Image.SCALE_SMOOTH);
            mem_stage = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/mem/mem.png")).
                    getScaledInstance(mem_width, height, Image.SCALE_SMOOTH);
            mem_beq = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/mem/branch.png")).
                    getScaledInstance(mem_width, height, Image.SCALE_SMOOTH);
            mem_lw = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/mem/lw.png")).
                    getScaledInstance(mem_width, height, Image.SCALE_SMOOTH);
            mem_sw = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/mem/sw.png")).
                    getScaledInstance(mem_width, height, Image.SCALE_SMOOTH);
            mem_r = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/mem/r-type.png")).
                    getScaledInstance(mem_width, height, Image.SCALE_SMOOTH);
            wb_stage = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/wb/wb.png")).
                    getScaledInstance(wb_width, height, Image.SCALE_SMOOTH);
            wb_ = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/wb/wb_.png")).
                    getScaledInstance(wb_width, height, Image.SCALE_SMOOTH);
            wb_write = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/wb/wb_write.png")).
                    getScaledInstance(wb_width, height, Image.SCALE_SMOOTH);

            previousIcon = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/previous.png")).
                    getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            nextIcon = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/next.png")).
                    getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            applyIcon = ImageIO.read(Images.class.getResourceAsStream("/Core/Images/apply.png")).
                    getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
