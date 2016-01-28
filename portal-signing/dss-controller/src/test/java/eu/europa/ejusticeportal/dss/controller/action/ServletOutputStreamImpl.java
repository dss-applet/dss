package eu.europa.ejusticeportal.dss.controller.action;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class ServletOutputStreamImpl extends ServletOutputStream{

    private final OutputStream os;
    public ServletOutputStreamImpl (OutputStream os){
        this.os = os;
    }
    @Override
    public void write(int b) throws IOException {
       os.write(b);
    }

}
