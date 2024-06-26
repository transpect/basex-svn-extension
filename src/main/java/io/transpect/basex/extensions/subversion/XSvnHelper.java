package io.transpect.basex.extensions.subversion;

import org.basex.query.value.node.FElem;
import org.basex.query.value.node.FBuilder;
import org.basex.query.value.item.QNm;

public class XSvnHelper  {
    public static FBuilder build (String name, String nsuri, String nsprefix){
        FBuilder fb = FElem.build(new QNm(name));
        fb.addNS(nsprefix.getBytes(),nsuri.getBytes());
        return fb;
    }

    public static FBuilder attach (FBuilder root, String name, String value){
        return root.add(new QNm(name),value);
    }
}