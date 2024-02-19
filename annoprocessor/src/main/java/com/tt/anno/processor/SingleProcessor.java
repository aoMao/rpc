package com.tt.anno.processor;

import com.tt.anno.Single;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class SingleProcessor extends AbstractProcessor {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.singleton(Single.class.getCanonicalName());
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(Single.class)) {

		}
		return true;
	}

	private void generateConstruct(Element element) {

	}
}
