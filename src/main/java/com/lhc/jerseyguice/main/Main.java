package com.lhc.jerseyguice.main;

import java.util.EnumSet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.Guice;
import com.google.inject.Scopes;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
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
        Guice.createInjector(
            new RestServletModule() {
				
				@Override
				protected void configureServlets() {
					bind(WelcomeTexter.class);
					bind(HelloWorldServlet.class).in(Scopes.SINGLETON);
					serve("/hello").with(HelloWorldServlet.class);
					rest("/app/*").packages("com.lhc.jerseyguice.service");
					bind(UserDao.class).to(UserDaoImpl.class);
					bind(TreeFolderDao.class).to(TreeFolderDaoImpl.class);
					bind(CanDao.class).to(CanDaoImpl.class);
					bind(CartDao.class).to(CartDaoImpl.class);
					bind(CategoryDao.class).to(CategoryDaoImpl.class);
					bind(ChiDao.class).to(ChiDaoImpl.class);
					bind(DescriptionDao.class).to(DescriptionDaoImpl.class);
					bind(DiscountDao.class).to(DiscountDaoImpl.class);
					bind(ImageDao.class).to(ImageDaoImpl.class);
					bind(MangDao.class).to(MangDaoImpl.class);
					bind(NotificationDao.class).to(NotificationDaoImpl.class);
					bind(PaymentDao.class).to(PaymentDaoImpl.class);
					bind(PlacesDao.class).to(PlacesDaoImpl.class);
					bind(ProductDao.class).to(ProductDaoImpl.class);
					bind(PromotionDao.class).to(PromotionDaoImpl.class);
					bind(SettingshopDao.class).to(SettingshopDaoImpl.class);
					bind(SizeDao.class).to(SizeDaoImpl.class);
					bind(StatusDao.class).to(StatusDaoImpl.class);
					bind(ThuoctinhDao.class).to(ThuoctinhDaoImpl.class);
					bind(TypeDao.class).to(TypeDaoImpl.class);
					bind(VoucherDao.class).to(VoucherDaoImpl.class);
					bind(WishlistDao.class).to(WishlistDaoImpl.class);
					bind(AppPhongThuyDao.class).to(AppPhongThuyDaoImpl.class);
					
				}
			}
        );
        
        int port = 5000;
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.addFilter(GuiceFilter.class, "/*", EnumSet.<javax.servlet.DispatcherType>of(javax.servlet.DispatcherType.REQUEST, javax.servlet.DispatcherType.ASYNC));
        context.addServlet(DefaultServlet.class, "/*");
        
        server.start();

        server.join();
    }
}
