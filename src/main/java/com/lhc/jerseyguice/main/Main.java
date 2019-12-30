package com.lhc.jerseyguice.main;

import java.util.EnumSet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.lhc.jerseyguice.context.HelloWorldServlet;
import com.lhc.jerseyguice.context.MyServletContextListener;
import com.lhc.jerseyguice.context.WelcomeTexter;
import com.lhc.jerseyguice.dao.AppPhongThuyDao;
import com.lhc.jerseyguice.dao.CanDao;
import com.lhc.jerseyguice.dao.CartDao;
import com.lhc.jerseyguice.dao.CategoryDao;
import com.lhc.jerseyguice.dao.ChiDao;
import com.lhc.jerseyguice.dao.DescriptionDao;
import com.lhc.jerseyguice.dao.DiscountDao;
import com.lhc.jerseyguice.dao.ImageDao;
import com.lhc.jerseyguice.dao.MangDao;
import com.lhc.jerseyguice.dao.NotificationDao;
import com.lhc.jerseyguice.dao.PaymentDao;
import com.lhc.jerseyguice.dao.PlacesDao;
import com.lhc.jerseyguice.dao.ProductDao;
import com.lhc.jerseyguice.dao.PromotionDao;
import com.lhc.jerseyguice.dao.SettingshopDao;
import com.lhc.jerseyguice.dao.SizeDao;
import com.lhc.jerseyguice.dao.StatusDao;
import com.lhc.jerseyguice.dao.ThuoctinhDao;
import com.lhc.jerseyguice.dao.TreeFolderDao;
import com.lhc.jerseyguice.dao.TypeDao;
import com.lhc.jerseyguice.dao.UserDao;
import com.lhc.jerseyguice.dao.VoucherDao;
import com.lhc.jerseyguice.dao.WishlistDao;
import com.lhc.jerseyguice.dao.impl.AppPhongThuyDaoImpl;
import com.lhc.jerseyguice.dao.impl.CanDaoImpl;
import com.lhc.jerseyguice.dao.impl.CartDaoImpl;
import com.lhc.jerseyguice.dao.impl.CategoryDaoImpl;
import com.lhc.jerseyguice.dao.impl.ChiDaoImpl;
import com.lhc.jerseyguice.dao.impl.DescriptionDaoImpl;
import com.lhc.jerseyguice.dao.impl.DiscountDaoImpl;
import com.lhc.jerseyguice.dao.impl.ImageDaoImpl;
import com.lhc.jerseyguice.dao.impl.MangDaoImpl;
import com.lhc.jerseyguice.dao.impl.NotificationDaoImpl;
import com.lhc.jerseyguice.dao.impl.PaymentDaoImpl;
import com.lhc.jerseyguice.dao.impl.PlacesDaoImpl;
import com.lhc.jerseyguice.dao.impl.ProductDaoImpl;
import com.lhc.jerseyguice.dao.impl.PromotionDaoImpl;
import com.lhc.jerseyguice.dao.impl.SettingshopDaoImpl;
import com.lhc.jerseyguice.dao.impl.SizeDaoImpl;
import com.lhc.jerseyguice.dao.impl.StatusDaoImpl;
import com.lhc.jerseyguice.dao.impl.ThuoctinhDaoImpl;
import com.lhc.jerseyguice.dao.impl.TreeFolderDaoImpl;
import com.lhc.jerseyguice.dao.impl.TypeDaoImpl;
import com.lhc.jerseyguice.dao.impl.UserDaoImpl;
import com.lhc.jerseyguice.dao.impl.VoucherDaoImpl;
import com.lhc.jerseyguice.dao.impl.WishlistDaoImpl;
import com.lhc.jerseyguice.model.User;
import com.lhc.jerseyguice.service.TreeFolderService;

import at.aberger.jerseyguice.config.RestServletModule;

public class Main {

	public static void main(String[] args) throws Exception {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addFilter(GuiceFilter.class, "/*", EnumSet.<javax.servlet.DispatcherType>of(
				javax.servlet.DispatcherType.REQUEST, javax.servlet.DispatcherType.ASYNC));
		context.addServlet(DefaultServlet.class, "/");
		
		
		Server jettyServer = new Server(8080);
		jettyServer.setHandler(context);
		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/app/*");
		jerseyServlet.setInitOrder(0);
		// Tells the Jersey Servlet which REST service/class to load.

		jerseyServlet.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "com.lhc.jerseyguice.service");

		try {
			jettyServer.start();
			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}
	}
}
