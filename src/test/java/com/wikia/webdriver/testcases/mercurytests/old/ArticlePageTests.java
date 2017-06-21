package com.wikia.webdriver.testcases.mercurytests.old;

import com.wikia.webdriver.common.contentpatterns.MercurySubpages;
import com.wikia.webdriver.common.contentpatterns.MercuryWikis;
import com.wikia.webdriver.common.contentpatterns.PageContent;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.annotations.InBrowser;
import com.wikia.webdriver.common.core.api.ArticleContent;
import com.wikia.webdriver.common.core.drivers.Browser;
import com.wikia.webdriver.common.core.helpers.ContentLoader;
import com.wikia.webdriver.common.core.helpers.Emulator;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.elements.common.Navigate;
import com.wikia.webdriver.elements.mercury.components.Loading;
import com.wikia.webdriver.elements.mercury.components.Navigation;
import com.wikia.webdriver.elements.mercury.components.TopBar;
import com.wikia.webdriver.elements.mercury.old.ArticlePageObject;

import org.testng.annotations.Test;

@Test(groups = "Mercury_Article")
@Execute(onWikia = MercuryWikis.MERCURY_AUTOMATION_TESTING)
@InBrowser(
    browser = Browser.CHROME,
    emulator = Emulator.GOOGLE_NEXUS_5
)
public class ArticlePageTests extends NewTestTemplate {

  private TopBar topBar;
  private Navigation navigation;
  private Navigate navigate;
  private Loading loading;

  private static final String MAIN_PAGE_CONTENT =
          ContentLoader.loadWikiTextContent("Mercury_MainPage");

  private void init() {
    this.topBar = new TopBar();
    this.navigation = new Navigation(driver);
    this.navigate = new Navigate();
    this.loading = new Loading(driver);
  }

  @Test(groups = "mercury_article_wikiaLogoTopContributorsSectionAndFooterElementsAreVisible")
  public void mercury_article_wikiaLogoTopContributorsSectionAndFooterElementsAreVisible() {
    new ArticleContent().clear();
    new ArticleContent().push(MAIN_PAGE_CONTENT, MercurySubpages.MAIN_PAGE);

    init();
    ArticlePageObject articlePage = new ArticlePageObject(driver);
    navigate.toPage(MercurySubpages.MAIN_PAGE);

    Assertion.assertTrue(articlePage.isWikiaLogoVisible());
    Assertion.assertTrue(articlePage.isSearchButtonVisible());
    Assertion.assertTrue(articlePage.isTopContributorsSectionVisible());
    Assertion.assertTrue(articlePage.isTopContributorsThumbVisible(0));
    Assertion.assertTrue(articlePage.isFooterVisible());
    Assertion.assertTrue(articlePage.isFooterLogoVisible());
  }

  @Test(groups = "mercury_article_linksInTopContributorsSectionRedirectsToUserPage")
  public void mercury_article_linksInTopContributorsSectionRedirectsToUserPage() {
    new ArticleContent().clear();
    new ArticleContent().push(MAIN_PAGE_CONTENT, MercurySubpages.MAIN_PAGE);

    init();
    ArticlePageObject articlePage = new ArticlePageObject(driver);
    navigate.toPage(MercurySubpages.MAIN_PAGE);

    articlePage.clickTopContributor(0);

    Assertion.assertTrue(articlePage.isUrlContainingUserPage());
  }

  @Test(groups = "mercury_article_linkedImagesRedirectToCorrespondingUrl")
  public void mercury_article_linkedImagesRedirectToCorrespondingUrl() {
    init();
    ArticlePageObject articlePage = new ArticlePageObject(driver);
    navigate.toPage(MercurySubpages.LINKED_IMAGES);

    String oldUrl = driver.getCurrentUrl();
    articlePage.clickOnImage(0);
    loading.handleAsyncPageReload();

    boolean result = !driver.getCurrentUrl().equals(oldUrl);
    PageObjectLogging.log(
        "Redirection",
        "works",
        "does not work",
        result
    );
  }

  @Test(groups = "mercury_article_navigateToArticlesWithColonAndQuestionMark")
  public void mercury_article_navigateToArticlesWithColonAndQuestionMark() {
    init();
    ArticlePageObject article = new ArticlePageObject(driver);

    String encodedQuestionMarkUrl = "/wiki/Question%3Fmark%3Fquestion";
    String encodedColonUrl = "/wiki/Colon%3Acolon%3Acolon";

    PageObjectLogging.logWarning(
        "Info",
        "Accessing article directly through URL"
    );

    navigate.toPage(encodedColonUrl);

    boolean result = driver.getCurrentUrl().contains(encodedColonUrl);
    PageObjectLogging.log(
        "URL for colon",
        "is encoded",
        "is not encoded",
        result
    );

    result = MercurySubpages.COLON.toLowerCase().contains(article.getArticleTitle().toLowerCase());
    PageObjectLogging.log(
        "Article title for colon",
        "is correct",
        "is not correct",
        result
    );

    navigate.toPage(encodedQuestionMarkUrl);

    result = driver.getCurrentUrl().contains(encodedQuestionMarkUrl);
    PageObjectLogging.log(
        "URL for question mark",
        "is encoded",
        "is not encoded",
        result
    );

    result = MercurySubpages.QUESTION_MARK.toLowerCase()
        .contains(article.getArticleTitle().toLowerCase());
    PageObjectLogging.log(
        "Article title for question mark",
        "is correct",
        "is not correct",
        result
    );

    PageObjectLogging.logWarning(
        "Info",
        "Accessing article through link in content"
    );

    article.clickOnAnchorInContent(0);
    loading.handleAsyncPageReload();

    result = !driver.getCurrentUrl().contains(encodedColonUrl);
    PageObjectLogging.log(
        "URL for colon",
        "is not encoded",
        "is encoded",
        result
    );

    result = MercurySubpages.COLON.toLowerCase().contains(article.getArticleTitle().toLowerCase());
    PageObjectLogging.log(
        "Article title for colon",
        "is correct",
        "is not correct",
        result
    );

    article.clickOnAnchorInContent(0);
    loading.handleAsyncPageReload();

    result = driver.getCurrentUrl().contains(encodedQuestionMarkUrl);
    PageObjectLogging.log(
        "URL for question mark",
        "is encoded",
        "is not encoded",
        result
    );

    result = MercurySubpages.QUESTION_MARK.toLowerCase().contains(
        article.getArticleTitle().toLowerCase());
    PageObjectLogging.log(
        "Article title for question mark",
        "is correct",
        "is not correct",
        result
    );

    PageObjectLogging.logWarning("Info", "Accessing article through link in navigation side");

    topBar.openNavigation();
    navigation.openSubMenu(3);
    navigation.openPageLink(5);

    result = !driver.getCurrentUrl().contains(encodedColonUrl);
    PageObjectLogging.log(
        "URL for colon",
        "is not encoded",
        "is encoded",
        result
    );

    result = MercurySubpages.COLON.toLowerCase().contains(article.getArticleTitle().toLowerCase());
    PageObjectLogging.log(
        "Article title for colon",
        "is correct",
        "is not correct",
        result
    );

    topBar.openNavigation();
    navigation.openSubMenu(3);
    navigation.openPageLink(4);

    result = driver.getCurrentUrl().contains(encodedQuestionMarkUrl);
    PageObjectLogging.log(
        "URL for question mark",
        "is encoded",
        "is not encoded",
        result
    );

    result = MercurySubpages.QUESTION_MARK.toLowerCase().contains(
        article.getArticleTitle().toLowerCase());
    PageObjectLogging.log(
        "Article title for question mark",
        "is correct",
        "is not correct",
        result
    );

    PageObjectLogging.logWarning("Info", "Accessing article through link in search result");

    topBar.openSearch().navigateToPage(MercurySubpages.COLON.substring(6));

    result = driver.getCurrentUrl().contains(encodedColonUrl);
    PageObjectLogging.log(
        "URL for colon",
        "is encoded",
        "is not encoded",
        result
    );

    result = MercurySubpages.COLON.toLowerCase().contains(article.getArticleTitle().toLowerCase());
    PageObjectLogging.log(
        "Article title for colon",
        "is correct",
        "is not correct",
        result
    );

    topBar.openSearch().navigateToPage(MercurySubpages.QUESTION_MARK.substring(6));

    result = driver.getCurrentUrl().contains(encodedQuestionMarkUrl);
    PageObjectLogging.log(
        "URL for question mark",
        "is encoded",
        "is not encoded",
        result
    );

    result = MercurySubpages.QUESTION_MARK.toLowerCase().contains(
        article.getArticleTitle().toLowerCase());
    PageObjectLogging.log(
        "Article title for question mark",
        "is correct",
        "is not correct",
        result
    );
  }
}
