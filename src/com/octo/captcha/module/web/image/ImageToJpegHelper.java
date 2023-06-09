/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.module.web.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Helper class
 *
 * @author <a href="mailto:marc.antoine.garrigue@gmail.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class ImageToJpegHelper {


    /**
     * retrieve a new ImageCaptcha using ImageCaptchaService and flush it to the response.<br/> Captcha are localized
     * using request locale.<br/>
     * <p/>
     * This method returns a 404 to the client instead of the image if the request isn't correct (missing parameters,
     * etc...)..<br/> The log may be null.<br/>
     *
     * @param theRequest  the request
     * @param theResponse the response
     * @param log         a commons logger
     * @param service     an ImageCaptchaService instance
     *
     * @throws java.io.IOException if a problem occurs during the jpeg generation process
     */
    public static void flushNewCaptchaToResponse(HttpServletRequest theRequest,
                                                 HttpServletResponse theResponse,
                                                 Object log,
                                                 ImageCaptchaService service,
                                                 String id,
                                                 Locale locale)
            throws IOException {

        // call the ImageCaptchaService method to retrieve a captcha
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            BufferedImage challenge =
                    service.getImageChallengeForID(id, locale);
            // the output stream to render the captcha image as jpeg into
           ImageIO.write(challenge, "png",jpegOutputStream);
        } catch (IllegalArgumentException e) {
                theResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
        } catch (CaptchaServiceException e) {
            theResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        // render the captcha challenge as a JPEG image in the response
        theResponse.setHeader("Cache-Control", "no-store");
        theResponse.setHeader("Pragma", "no-cache");
        theResponse.setDateHeader("Expires", 0);

        theResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                theResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }


}
