package com.octo.captcha.component.image.textpaster.textvisitor;

import com.octo.captcha.component.image.textpaster.MutableAttributedString;

/**
 * @author mag
 * @Date 5 mars 2008
 */
public interface TextVisitor {
   void visit(MutableAttributedString mas); 
}
