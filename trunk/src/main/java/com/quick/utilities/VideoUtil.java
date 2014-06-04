/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.quick.utilities;

/**
 *
 * @author sateri
 */
public class VideoUtil 
{
    public static String getYoutubeEmbedingString(String video)
    {
        if (video.indexOf("=") > 0) {
                video = video.substring(video.lastIndexOf("=") + 1);
            } else {
                video = video.substring(video.lastIndexOf("/") + 1);
            }
            //System.out.println("********videoId="+videoId);
            String str = "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "  <body>\n"
                    + "    <!-- 1. The <iframe> (and video player) will replace this <div> tag. -->\n"
                    + "<iframe id=\"player\" type=\"text/html\" width=\"400\" height=\"250\"\n"
                    + "  src=\"https://www.youtube.com/embed/" + video + "?enablejsapi=1&origin=https://example.com\"\n"
                    + "  frameborder=\"0\"></iframe>"
                    + "\n"
                    + "    <script>\n"
                    + "      // 2. This code loads the IFrame Player API code asynchronously.\n"
                    + "      var tag = document.createElement('script');\n"
                    + "\n"
                    + "      tag.src = \"https://www.youtube.com/iframe_api\";\n"
                    + "      var firstScriptTag = document.getElementsByTagName('script')[0];\n"
                    + "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n"
                    + "\n"
                    + "      // 3. This function creates an <iframe> (and YouTube player)\n"
                    + "      //    after the API code downloads.\n"
                    + "      var player;\n"
                    + "      function onYouTubeIframeAPIReady() {\n"
                    + "        player = new YT.Player('player', {\n"
                    + "          height: '250',\n"
                    + "          width: '400',\n"
                    + "          videoId: '" + video + "',\n"
                    + "          events: {\n"
                    + "            'onReady': onPlayerReady,\n"
                    + "            'onStateChange': onPlayerStateChange\n"
                    + "          }\n"
                    + "        });\n"
                    + "      }\n"
                    + "\n"
                    + "      // 4. The API will call this function when the video player is ready.\n"
                    + "      function onPlayerReady(event) {\n"
                    + "        event.target.playVideo();\n"
                    + "      }\n"
                    + "\n"
                    + "      // 5. The API calls this function when the player's state changes.\n"
                    + "      //    The function indicates that when playing a video (state=1),\n"
                    + "      //    the player should play for six seconds and then stop.\n"
                    + "      var done = false;\n"
                    + "      function onPlayerStateChange(event) {\n"
                    + "        if (event.data == YT.PlayerState.PLAYING && !done) {\n"
                    + "          setTimeout(stopVideo, 6000);\n"
                    + "          done = true;\n"
                    + "        }\n"
                    + "      }\n"
                    + "      function stopVideo() {\n"
                    + "        player.stopVideo();\n"
                    + "      }\n"
                    + "    </script>\n"
                    + "  </body>\n"
                    + "</html>";
            
            return str;
    }
    
}
