package jason.self.fms.util;

import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;

import jason.self.fms.service.schedule.Schedule;


public class JibxHelper {
	private static final String DEFAULT_ENCODING = "UTF-8";
	static public String toXML(Object objToMarshal) throws JiBXException {
		StringWriter out = new StringWriter();

		IBindingFactory bfact = BindingDirectory.getFactory(
				objToMarshal.getClass());
		IMarshallingContext mctx = bfact.createMarshallingContext();
		mctx.setIndent(4);
//		mctx.setOutput(out, UTF8EscaperIgnoreIllegalChar.getInstance());
		mctx.marshalDocument(objToMarshal, DEFAULT_ENCODING, null);
		return out.toString();
	}
	public static Schedule unmarshalXML(String xml, Class<Schedule> class1) {
		// TODO Auto-generated method stub
		return null;
	}
}
