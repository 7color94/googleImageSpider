package palm.thread;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import palm.entity.AgeFace;
import palm.entity.Image;
import palm.repertory.Repertory;
import palm.util.Configuration;
import palm.util.Connection;

import java.net.URLEncoder;

public class ImageUrlThread implements Runnable {

    private static Logger logger = LogManager.getLogger(ImageUrlThread.class.getSimpleName());

    public AgeFace takeAgeFaceFromDeque() {
        AgeFace ageFace = null;
        try {
            ageFace = Repertory.ageFaces.take();
        } catch (Exception e) {
            logger.error("Take ageFace from Deque Error." + e.getMessage());
        }
        return ageFace;
    }

    public void putAgeFaceToDeque(AgeFace ageFace) {
        try {
            Repertory.ageFaces.put(ageFace);
        } catch (Exception e) {
            logger.error("Put ageFace to Deque Error." + e.getMessage());
        }
    }

    public void putImagesToDeque(Image e) {
        try{
            Repertory.images.put(e);
        } catch (Exception ee) {
            logger.error("Put Image to Deque Error." + ee.getMessage());
        }
    }

    public void run() {
        while (true) {
            AgeFace ageFace = takeAgeFaceFromDeque();
            if (ageFace == null || ageFace.getAge() == 0) {
                continue;
            }
            for (int start = ageFace.getStart(); start <= Configuration.imageNum; start += 100) {
                boolean connectOk = true;
                String getUrl = null;
                try {
                    getUrl = Configuration.imageAjaxUrlHead + URLEncoder.encode(String.valueOf(ageFace.getAge()), "UTF-8")
                            + Configuration.imageAjaxUrlBody + URLEncoder.encode(String.valueOf(start), "UTF-8")
                            + Configuration.imageAjaxUrlEnd + URLEncoder.encode(String.valueOf(start / 100), "UTF-8");
                } catch (Exception e) {
                    logger.error("Encode" + ageFace.getAge() + ", " + ageFace.getStart() + " Error." + e.getMessage());
                }
                if (getUrl == null) {
                    connectOk = false;
                    ageFace.setStart(start);
                    putAgeFaceToDeque(ageFace);
                    break;
                }
                logger.info(getUrl);
                String httpConResult = Connection.get(getUrl);
                if (httpConResult == null) {
                    connectOk = false;
                    ageFace.setStart(start);
                    putAgeFaceToDeque(ageFace);
                    break;
                }
                if (connectOk) {
                    String[] imageUrls = StringUtils.substringsBetween(httpConResult, "src=\\\"", "\\\"");
                    logger.info("Find " + imageUrls.length + " images, start = " + start);
                    int count = 0;
                    for (String iu: imageUrls) {
                        if (count < 5) {
                            logger.info(iu);
                            count++;
                        }
                        Image ima = new Image(iu.hashCode(), ageFace.getAge(), iu);
                        putImagesToDeque(ima);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
//        String[] ss = StringUtils.substringsBetween(s, "src=\\\"", "\\\"");
//        String s = "[\"rg_s\",[\"dom\",\"\\u003Cstyle\\u003E.rg_kn,.rg_s{}.rg_bx{display:-moz-inline-box;display:inline-block;margin-top:0;margin-right:12px;margin-bottom:12px;margin-left:0;overflow:hidden;position:relative;vertical-align:top;z-index:1}.rg_meta{display:none}.rg_l{display:inline-block;height:100%;position:absolute;text-decoration:none;width:100%}.rg_l:focus{outline:0}.rg_i{border:0;color:rgba(0,0,0,0);display:block;-webkit-touch-callout:none;}.rg_an,.rg_anbg,.rg_ilm,.rg_ilmbg{right:0;bottom:0;box-sizing:border-box;-webkit-box-sizing:border-box;color:#fff;font:normal 11px arial,sans-serif;line-height:100%;white-space:nowrap;width:100%}.rg_anbg,.rg_ilmbg{background:rgba(51,51,51,0.8);margin-left:0;padding:2px 4px;position:absolute}.rg_ilmn{bottom:0;display:block;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}.rg_ilm{display:none}#rg_s.rg_kn .rg_l:focus .rg_ilm{display:block}.rg_kn .rg_bx:hover .rg_ilm,.rg_bx:hover .rg_anbg{display:none}.rg_bx:hover .rg_ilm,.rg_anbg,.rg_kn .rg_bx:hover .rg_anbg{display:block}\\u003C\\/style\\u003E\\u003Cdiv eid=\\\"S42qV52gLYuYwgSP5bqgDw\\\" id=\\\"isr_scm_8\\\" style=\\\"display:none\\\"\\u003E\\u003C\\/div\\u003E\\u003Cdiv data-cei=\\\"S42qV52gLYuYwgSP5bqgDw\\\" class=\\\"rg_add_chunk\\\"\\u003E\\u003C!--m--\\u003E\\u003Cdiv class=\\\"rg_di rg_bx rg_el ivg-i\\\" data-ved=\\\"0ahUKEwjd8YPU4rXOAhULjJAKHY-yDvQQMwgCKAAwAA\\\"\\u003E\\u003Ca jsaction=\\\"fire.ivg_o;mouseover:str.hmov;mouseout:str.hmou\\\" class=\\\"rg_l\\\" style=\\\"background:rgb(120,117,107)\\\"\\u003E\\u003Cimg data-sz=\\\"f\\\" name=\\\"rT5EFE_1GCi_9M:\\\" src=\\\"https:\\/\\/encrypted-tbn0.gstatic.com\\/images?q=tbn:ANd9GcRG5Wy3FWF0qAS0oCOmlFD8834aaz48yjwOKL5V5MFkUqrLAfoZXg\\\" class=\\\"rg_i rg_ic\\\" alt=\\\"Image result for 70 years old face\\\" jsaction=\\\"load:str.tbn\\\" onload=\\\"google.aft\\u0026\\u0026google.aft(this)\\\"\\u003E\\u003Cdiv class=\\\"_aOd rg_ilm\\\"\\u003E\\u003Cdiv class=\\\"rg_ilmbg\\\"\\u003E\\u003Cspan class=\\\"rg_ilmn\\\"\\u003E 624\\u0026nbsp;\\u0026#215;\\u0026nbsp;351 - bbc.com \\u003C\\/span\\u003E\\u003C\\/div\\u003E\\u003C\\/div\\u003E\\u003C\\/a\\u003E\\u003Cdiv class=\\\"rg_meta\\\"\\u003E{\\\"clt\\\":\\\"n\\\",\\\"id\\\":\\\"rT5EFE_1GCi_9M:\\\",\\\"isu\\\":\\\"bbc.com\\\",\\\"itg\\\":false,\\\"ity\\\":\\\"jpg\\\",\\\"oh\\\":351,\\\"ou\\\":\\\"http:\\/\\/ichef.bbci.co.uk\\/news\\/624\\/cpsprodpb\\/DCB4\\/production\\/_84700565_db19d7f5-ebcd-4add-bd44-b2c89acfd99f.jpg\\\",\\\"ow\\\":624,\\\"pt\\\":\\\"Hiroshima marks 70 years since atomic bomb - BBC News\\\",\\\"rid\\\":\\\"tANjkWZQaBnY2M\\\",\\\"ru\\\":\\\"http:\\/\\/www.bbc.com\\/news\\/world-asia-33792789\\\",\\\"s\\\":\\\"Doves fly over the Atomic Bomb Dome during the peace memorial ceremony marking the 70th anniversary\\\",\\\"th\\\":168,\\\"tu\\\":\\\"https:\\/\\/encrypted-tbn0.gstatic.com\\/images?q\\\\u003dtbn:ANd9GcRG5Wy3FWF0qAS0oCOmlFD8834aaz48yjwOKL5V5MFkUqrLAfoZXg\\\",\\\"tw\\\":300}\\u003C\\/div\\u003E\\u003C\\/div\\u003E\\u003C!--n--\\u003E\\u003C!--m--\\u003E\\u003Cdiv class=\\\"rg_di rg_bx rg_el ivg-i\\\" data-ved=\\\"0ahUKEwjd8YPU4rXOAhULjJAKHY-yDvQQMwgDKAEwAQ\\\"\\u003E\\u003Ca jsaction=\\\"fire.ivg_o;mouseover:str.hmov;mouseout:str.hmou\\\" class=\\\"rg_l\\\" style=\\\"background:rgb(168,107,85)\\\"\\u003E\\u003Cimg data-sz=\\\"f\\\" name=\\\"WE18xekxBXHJyM:\\\" src=\\\"https:\\/\\/encrypted-tbn0.gstatic.com\\/images?q=tbn:ANd9GcS3uod4fZteFtQIS6ynO-ZD4pqw7bFcaOG1bPWLfLSyvVMJ-Zq6iQ\\\" class=\\\"rg_i rg_ic\\\" alt=\\\"Image result for 70 years old face\\\" jsaction=\\\"load:str.tbn\\\" onload=\\\"google.aft\\u0026\\u0026google.aft(this)\\\"\\u003E\\u003Cdiv class=\\\"_aOd rg_ilm\\\"\\u003E\\u003Cdiv class=\\\"rg_ilmbg\\\"\\u003E\\u003Cspan class=\\\"rg_ilmn\\\"\\u003E 640\\u0026nbsp;\\u0026#215;\\u0026nbsp;427 - rihannanow.com \\u003C\\/span\\u003E\\u003C\\/div\\u003E\\u003C\\/div\\u003E\\u003C\\/a\\u003E\\u003Cdiv class=\\\"rg_meta\\\"\\u003E{\\\"cl\\\":3,\\\"clt\\\":\\\"n\\\",\\\"id\\\":\\\"WE18xekxBXHJyM:\\\",\\\"isu\\\":\\\"rihannanow.com\\\",\\\"itg\\\":false,\\\"ity\\\":\\\"jpg\\\",\\\"oh\\\":427,\\\"ou\\\":\\\"http:\\/\\/www.rihannanow.com\\/wp-content\\/uploads\\/2014\\/06\\/2014-CFDA-5651x3768-640x427.jpg\\\",\\\"ow\\\":640,\\\"pt\\\":\\\"Bio | Rihanna\\\",\\\"rid\\\":\\\"ZT3F1dvJTbA3kM\\\",\\\"ru\\\":\\\"http:\\/\\/www.rihannanow.com\\/bio\\/\\\",\\\"s\\\":\\\"... her over 70 magazine covers. she was named the face of french fashion house balmain\\\\u0026#39;s spring\\/summer 2014 campaign and received the prestigious fashion ...\\\",\\\"th\\\":183,\\\"tu\\\":\\\"https:\\/\\/encrypted-tbn0.gstatic.com\\/images?q\\\\u003dtbn:ANd9GcS3uod4fZteFtQIS6ynO-ZD4pqw7bFcaOG1bPWLfLSyvVMJ-Zq6iQ\\\",\\\"tw\\\":275}\\u003C\\/div\\u003E\\u003C\\/div\\u003E\\u003C!--n--\\u003E\\u003C!--m--\\u003E\\u003Cdiv class=\\\"rg_di rg_bx rg_el ivg-i\\\" data-ved=\\\"0ahUKEwjd8YPU4rXOAhULjJAKHY-yDvQQMwgEKAIwAg\\\"\\u003E\\u003Ca jsaction=\\\"fire.ivg_o;mouseover:str.hmov;mouseout:str.hmou\\\" class=\\\"rg_l\\\" style=\\\"background:rgb(240,2" +
//                "40,208)\\\"\\u003E\\u003Cimg data-sz=\\\"f\\\" name=\\\"gCj9eyFGhN7ZyM:\\\"";
//        for (String t: ss) {
//            System.out.println(t);
//        }
        String s = "https:\\/\\/encrypted-tbn0.gstatic.com\\/images?q=tbn:ANd9GcRG5Wy3FWF0qAS0oCOmlFD8834aaz48yjwOKL5V5MFkUqrLAfoZXg";
        String ss = "zhuyao";
        System.out.println(s.hashCode());
        System.out.println(ss.hashCode());
    }
}
