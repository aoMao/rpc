package com.tt.anno.processor;

import com.google.auto.common.AnnotationMirrors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MessageIdGenerateConstProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton("com.tt.message.anno.Message");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getRootElements()) {
            try {
                generateConst(element);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public void generateConst(Element element) throws IOException {
        var message = element.getAnnotationMirrors().stream().filter(annotationMirror -> {
            var ele = annotationMirror.getAnnotationType().asElement();
            return "Message".equals(ele.getSimpleName().toString());
        }).findFirst();
        boolean hasMsg = message.isPresent();
        if (!hasMsg) {
            return;
        }

        var curClzName = element.getSimpleName().toString();
        var curPackageName = getPackageName(element);
        var clzName = curClzName + "IdConst";
        var packageName = curPackageName + ".constant";
        var file = this.processingEnv.getFiler().createSourceFile(String.format("%s.%s", packageName, clzName));
        Writer writer = file.openWriter();
        writer.write(String.format("package %s;%s", packageName, "\n\n"));
        writer.write("\n");

        writer.write(String.format("import %s.%s;%s", curPackageName, curClzName, "\n\n"));
        writer.write(String.format("public class %s {\n", clzName));

        int startId = getMessageStartId(message.get());
        for (Element enclosedElement : ElementFilter.methodsIn(element.getEnclosedElements())) {
            if (ElementKind.METHOD != enclosedElement.getKind()) {
                continue;
            }
            var methodName = enclosedElement.getSimpleName().toString();
            var name = parseMethodName(methodName);
            int id = startId++;
            writer.write("\t/**\n");
            writer.write(
                    String.format("\t * {@link %s#%s}\n", element.getSimpleName().toString(), methodName));
            writer.write("\t */\n");
            writer.write(String.format("\tpublic static int %s = %d;\n", name, id));

            id = startId++;
            writer.write("\t/**\n");
            writer.write(
                    String.format("\t * {@link %s#%s} result\n", element.getSimpleName().toString(), methodName));
            writer.write("\t */\n");
            writer.write(String.format("\tpublic static int %s_RESULT = %d;\n", name, id));
        }
        writer.write("}");
        writer.flush();
        writer.close();
    }

    private int getMessageStartId(AnnotationMirror annotationMirror) {
        return (int) AnnotationMirrors.getAnnotationValue(annotationMirror, "startId").getValue();
    }

    public String getPackageName(Element element) {
        String name = element.getSimpleName().toString();
        var fullName = element.asType().toString();
        return fullName.substring(0, fullName.length() - name.length() - 1);
    }

    public static String parseMethodName(String simpleName) {
        var builder = new StringBuilder();
        for (char c : simpleName.toCharArray()) {
            if (Character.isUpperCase(c)) {
                builder.append("_");
            }
            builder.append(Character.toUpperCase(c));
        }
        return builder.toString();
    }

    public void logError(String msg) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }
}
