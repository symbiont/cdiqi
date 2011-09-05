package cz.symbiont_it.cdiqi.tests;

import java.io.File;

import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import cz.symbiont_it.cdiqi.api.Asynchronous;
import cz.symbiont_it.cdiqi.impl.QuartzSchedulerManagerImpl;
import cz.symbiont_it.cdiqi.impl.weld.WeldBoundRequestJobListener;
import cz.symbiont_it.cdiqi.spi.BoundRequestJobListener;

/**
 * 
 * @author Martin Kouba
 */
public final class CdiqiTestUtil {

	private CdiqiTestUtil() {
	}

	/**
	 * @return default test archive
	 */
	public static WebArchive createTestArchive() {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war");

		// API
		archive.addPackage(Asynchronous.class.getPackage());
		// SPI
		archive.addPackage(BoundRequestJobListener.class.getPackage());
		// Core impl
		archive.addPackage(QuartzSchedulerManagerImpl.class.getPackage());
		// Weld impl
		archive.addPackage(WeldBoundRequestJobListener.class.getPackage());
		
		archive.addClass(ResultStore.class);
		archive.addAsLibraries(DependencyResolvers
				.use(MavenDependencyResolver.class)
				.artifact("org.quartz-scheduler:quartz:2.0.2")
				.artifact("org.slf4j:slf4j-log4j12:1.6.1")
				.resolveAs(GenericArchive.class));
		archive.addAsResource(new File(
				"src/test/resources/cdiqi-quartz.properties"));
		archive.addAsResource(new File("src/test/resources/log4j.properties"));
		archive.setWebXML(new File("src/test/webapp/WEB-INF/web.xml"));
		archive.addAsWebInfResource(new File("src/test/resources/beans.xml"));
		// archive.addAsWebInfResource(EmptyAsset.INSTANCE,
		// ArchivePaths.create("beans.xml"));
		// ARQ will replace in the end
		// archive.setManifest(new
		// File("src/test/webapp/META-INF/MANIFEST.MF"));
		return archive;
	}

}
