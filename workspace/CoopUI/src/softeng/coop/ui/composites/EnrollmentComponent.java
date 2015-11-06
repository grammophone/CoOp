package softeng.coop.ui.composites;

import java.util.Locale;

import softeng.coop.business.authentication.IUser;
import softeng.coop.ui.ICoopContext;
import softeng.coop.ui.presenters.StudentEnrollmentPresenter;
import softeng.coop.ui.viewdefinitions.IEnrollmentView;
import softeng.ui.vaadin.mvp.IViewListener;
import softeng.ui.vaadin.mvp.Presenter;
import softeng.ui.vaadin.mvp.ViewEvent;
import softeng.ui.vaadin.mvp.ViewEventSubscription;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EnrollmentComponent 
	extends CoopComponent<IUser>
	implements IEnrollmentView
{
	@AutoGenerated
	private VerticalLayout mainLayout;

	@AutoGenerated
	private HorizontalLayout buttonsHorizontalLayout;

	@AutoGenerated
	private Button acceptButton;

	@AutoGenerated
	private Button termsOfuseButton;

	@AutoGenerated
	private Form userDataForm;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private static final long serialVersionUID = 1L;
	
	private ViewEventSubscription<IUser, ICoopContext, IEnrollmentView> acceptSubscrption = 
		new ViewEventSubscription<IUser, ICoopContext, IEnrollmentView>();
	
	private ViewEventSubscription<IUser, ICoopContext, IEnrollmentView> registrationSucceededSubscription =
		new ViewEventSubscription<IUser, ICoopContext, IEnrollmentView>();
	
	private TextField schoolTextField = new TextField();
	private TextField firstNameTextField = new TextField();
	private TextField lastNameTextField = new TextField();
	private TextField userNameTextField = new TextField();

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public EnrollmentComponent()
	{
		buildMainLayout();
		setCompositionRoot(mainLayout);

	}

	@Override
	protected Presenter<IUser, ICoopContext, IEnrollmentView> supplyPresenter()
	{
		return new StudentEnrollmentPresenter(this);
	}

	@Override
	public void dataBind()
	{
		BeanItem<IUser> userItem = new BeanItem<IUser>(this.getModel());
		this.userDataForm.setItemDataSource(userItem);
	}

	@SuppressWarnings("serial")
	@Override
	protected void setupUI()
	{
		super.setupUI();
		
		acceptButton.setEnabled(false);
		
		// Setup the form fields.
		this.userDataForm.setFormFieldFactory(new FormFieldFactory()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Field createField(Item item, Object propertyId, Component uiContext)
			{
				if (propertyId.equals("organizationUnit"))
				{
					schoolTextField.setWidth("100%");
					schoolTextField.setReadOnly(true);
					
					return schoolTextField;
				}
				else if (propertyId.equals("firstName"))
				{
					firstNameTextField.setWidth("100%");
					firstNameTextField.setReadOnly(true);
					
					return firstNameTextField;
				}
				else if (propertyId.equals("lastName"))
				{
					lastNameTextField.setWidth("100%");
					lastNameTextField.setReadOnly(true);
					
					return lastNameTextField;
				}
				else if (propertyId.equals("userName"))
				{
					userNameTextField.setWidth("100%");
					userNameTextField.setReadOnly(true);
					
					return userNameTextField;
				}
				return null;
			}
		});

		// "Terms of use" button handling.
		termsOfuseButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				getApplication();
				
				final Window termsWindow = 
					new Window(getLocalizedString("TERMS_OF_USE_CAPTION"));
				
				termsWindow.setWidth("480px");
				termsWindow.setHeight("320px");
				
				termsWindow.setClosable(false);
				termsWindow.setModal(true);
				
				VerticalLayout layout = new VerticalLayout();
				layout.setSpacing(true);
				layout.setMargin(true);

				TextArea textArea = new TextArea();
				textArea.setValue(getLocalizedString("TERMS_OF_USE_TEXT"));
				textArea.setSizeFull();
				layout.addComponent(textArea);
				layout.setExpandRatio(textArea, 1.0f);
				
				Button closeButton = new Button(getLocalizedString("CLOSE_CAPTION"));
				closeButton.addListener(new Button.ClickListener()
				{
					@Override
					public void buttonClick(ClickEvent event)
					{
						getApplication().getMainWindow().removeWindow(termsWindow);
						acceptButton.setEnabled(true);
					}
				});
				layout.addComponent(closeButton);
				layout.setExpandRatio(closeButton, 0.0f);
				layout.setComponentAlignment(closeButton, Alignment.BOTTOM_RIGHT);
				
				termsWindow.setContent(layout);
				
				layout.setSizeFull();
				
				getApplication().getMainWindow().addWindow(termsWindow);
				
			}
		});
		
		final EnrollmentComponent me = this;
		
		acceptButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				acceptSubscrption.fire(new ViewEvent<IUser, ICoopContext, IEnrollmentView>(me));
			}
		});
	
	}

	@Override
	protected void setupLocalizedCaptions(Locale locale)
	{
		super.setupLocalizedCaptions(locale);
		
		this.userDataForm.setCaption(this.getLocalizedString("USER_DATA"));
		
		this.schoolTextField.setCaption(getLocalizedString("SCHOOL_NAME"));
		this.firstNameTextField.setCaption(getLocalizedString("NAME"));
		this.lastNameTextField.setCaption(getLocalizedString("SURNAME"));
		this.userNameTextField.setCaption(getLocalizedString("USER_NAME"));
		
		this.termsOfuseButton.setCaption(getLocalizedString("TERMS_OF_USE"));
		this.acceptButton.setCaption(getLocalizedString("ACCEPT"));
		
	}

	@Override
	public void addAcceptListener(IViewListener<IUser, ICoopContext, IEnrollmentView> listener)
	{
		this.acceptSubscrption.add(listener);
		
	}

	@Override
	public void removeAcceptListener(IViewListener<IUser, ICoopContext, IEnrollmentView> listener)
	{
		this.acceptSubscrption.remove(listener);
	}

	@Override
	public void addRegistrationSucceededListener(IViewListener<IUser, ICoopContext, IEnrollmentView> listener)
	{
		this.registrationSucceededSubscription.add(listener);
	}

	@Override
	public void removeRegistrationSucceededListener(IViewListener<IUser, ICoopContext, IEnrollmentView> listener)
	{
		this.registrationSucceededSubscription.remove(listener);
	}

	@Override
	public void fireRegistrationSucceeded()
	{
		this.registrationSucceededSubscription.fire(new ViewEvent<IUser, ICoopContext, IEnrollmentView>(this));
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout()
	{
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// userDataForm
		userDataForm = new Form();
		userDataForm.setCaption("��������");
		userDataForm.setImmediate(false);
		userDataForm.setWidth("100.0%");
		userDataForm.setHeight("100.0%");
		mainLayout.addComponent(userDataForm);
		mainLayout.setExpandRatio(userDataForm, 1.0f);
		
		// buttonsHorizontalLayout
		buttonsHorizontalLayout = buildButtonsHorizontalLayout();
		mainLayout.addComponent(buttonsHorizontalLayout);
		mainLayout.setComponentAlignment(buttonsHorizontalLayout, new Alignment(10));
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildButtonsHorizontalLayout()
	{
		// common part: create layout
		buttonsHorizontalLayout = new HorizontalLayout();
		buttonsHorizontalLayout.setImmediate(false);
		buttonsHorizontalLayout.setWidth("-1px");
		buttonsHorizontalLayout.setHeight("-1px");
		buttonsHorizontalLayout.setMargin(false);
		buttonsHorizontalLayout.setSpacing(true);
		
		// termsOfuseButton
		termsOfuseButton = new Button();
		termsOfuseButton.setCaption("���� �������");
		termsOfuseButton.setImmediate(true);
		termsOfuseButton.setDescription("�������� ������������ ��� ���������� ���� ����� ���� ����������");
		termsOfuseButton.setWidth("110px");
		termsOfuseButton.setHeight("-1px");
		buttonsHorizontalLayout.addComponent(termsOfuseButton);
		
		// acceptButton
		acceptButton = new Button();
		acceptButton.setCaption("�������");
		acceptButton.setImmediate(true);
		acceptButton.setDescription("��������� ������� ��� �� ����������");
		acceptButton.setWidth("110px");
		acceptButton.setHeight("-1px");
		buttonsHorizontalLayout.addComponent(acceptButton);
		
		return buttonsHorizontalLayout;
	}

}