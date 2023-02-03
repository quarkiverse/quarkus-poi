package io.quarkiverse.poi.runtime.graal;

import java.util.Map;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xpath.Path;
import org.apache.xmlbeans.impl.xpath.XPathFactory;
import org.apache.xmlbeans.impl.xpath.XQuery;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(XPathFactory.class)
public final class XPathFactorySubstitution {

    @Substitute
    public static Path getCompiledPathSaxon(String pathExpr, String currentVar, Map<String, String> namespaces) {
        throw new UnsupportedOperationException("Saxon is not supported in native mode");
    }

    @Substitute
    static synchronized XQuery getCompiledQuery(String queryExpr, String currentVar, XmlOptions options) {
        throw new UnsupportedOperationException("Saxon is not supported in native mode");
    }
}
