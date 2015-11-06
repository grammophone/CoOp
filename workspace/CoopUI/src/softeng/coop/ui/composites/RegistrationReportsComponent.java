package softeng.coop.ui.composites;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import softeng.coop.dataaccess.AuthenticatedUser;
import softeng.coop.dataaccess.CoOp;
import softeng.coop.dataaccess.GradePolicyType;
import softeng.coop.dataaccess.Registration;
import softeng.coop.dataaccess.Student;
import softeng.coop.ui.ICoopContext;
import softeng.coop.ui.forms.RegistrationForm;
import softeng.coop.ui.viewdefinitions.IFormView;
import softeng.ui.vaadin.mvp.IView;
import softeng.ui.vaadin.mvp.Presenter;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class RegistrationReportsComponent 
extends CoopComponent<BeanItem<Registration>>
implements IFormView<BeanItem<Registration>>
{
	@AutoGenerated
	private VerticalLayout mainLayout;

	@AutoGenerated
	private RegistrationForm form;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private static final long serialVersionUID = 1L;
	
	private RegistrationReportsTableComponent reportsTableComponent;
		
	private TextField gradeTextField;
	
	private CheckBox passedCheckBox;
	
	private static List<String> propertyIds = createPropertyIds(); 

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public RegistrationReportsComponent()
	{
		buildMainLayout();
		setCompositionRoot(mainLayout);

	}
	
	private static List<String> createPropertyIds()
	{
		ArrayList<String> propertyIds = new ArrayList<String>();
		
		propertyIds.add("reports");
		propertyIds.add("grade");
		propertyIds.add("passed");
		
		return propertyIds;
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
		
		// form
		form = new RegistrationForm();
		form.setImmediate(false);
		form.setWidth("100.0%");
		form.setHeight("-1px");
		mainLayout.addComponent(form);
		
		return mainLayout;
	}

	@Override
	public boolean isDataValid()
	{
		return form.isValid();
	}

	@Override
	public void discardChanges()
	{
		form.discard();
	}

	@Override
	public void commitChangesToModel()
	{
		form.commit();
	}

	@Override
	protected Presenter<BeanItem<Registration>, ICoopContext, ? extends IView<BeanItem<Registration>, ICoopContext>> supplyPresenter()
	{
		return null;
	}

	@Override
	public void dataBind()
	{
		form.setItemDataSource(getModel(), propertyIds);
	}
	
	@Override
	protected void setupUI()
	{
		super.setupUI();
		
		form.setImmediate(true);
		form.setWriteThrough(false);
		
		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		
		form.setLayout(formLayout);
		
		form.setFormFieldFactory(new FormFieldFactory()
		{
			@Override
			public Field createField(Item item, Object propertyId, Component uiContext)
			{
				if (propertyId.equals("reports"))
				{
					reportsTableComponent = new RegistrationReportsTableComponent();
					
					reportsTableComponent.setWidth("100%");
					reportsTableComponent.setCaption(getLocalizedString("REPORTS_CAPTION"));
					reportsTableComponent.setParentModel(getModel().getBean());
					
					return reportsTableComponent;
				}
				else if (propertyId.equals("grade"))
				{
					CoOp currentCoop = getModel().getBean().getCoop();
					
					if (currentCoop == null) return null;
					
					if (currentCoop.getGradePolicy() == GradePolicyType.HasGrade 
							&& (!currentCoop.isGroupCoOp() || !currentCoop.isHasGroupGrade()))
					{
						AuthenticatedUser currentUser = getContext().getSession().getAuthenticatedUser();
						
						gradeTextField = new TextField();
					
						gradeTextField.setWidth("100%");
						gradeTextField.setCaption(getLocalizedString("GRADE_CAPTION"));
						gradeTextField.setReadOnly(currentUser instanceof Student);
						gradeTextField.setNullRepresentation("");
						gradeTextField.setNullSettingAllowed(true);
						
						gradeTextField.addListener(new Property.ValueChangeListener()
						{
							@Override
							public void valueChange(ValueChangeEvent event)
							{
								if (passedCheckBox == null) return;
								
								String gradeString = (String)event.getProperty().getValue();
								
								Float grade = null;
								
								try
								{
									grade = Float.parseFloat(gradeString);
								}
								catch (Exception ex)
								{
									return;
								}
								
								if (grade == null) return;
								
								passedCheckBox.setValue(grade >= 5.0f);
							}
						});
						
						return gradeTextField;
					}
				}
				else if (propertyId.equals("passed"))
				{
					CoOp currentCoop = getModel().getBean().getCoop();
					
					if (currentCoop == null) return null;
					
					if (!currentCoop.isGroupCoOp() || !currentCoop.isHasGroupGrade())
					{
						passedCheckBox = new CheckBox();
						
						AuthenticatedUser currentUser = getContext().getSession().getAuthenticatedUser();
	
						passedCheckBox.setReadOnly(currentUser instanceof Student);
	
						passedCheckBox.setCaption(getLocalizedString("PASSED_CAPTION"));
						passedCheckBox.setWidth("100%");
						
						return passedCheckBox;
					}
				}
				
				return null;
			}
		});
	}

	@Override
	protected void setupLocalizedCaptions(Locale locale)
	{
		super.setupLocalizedCaptions(locale);
		
		form.setCaption(getLocalizedString("FORM_CAPTION"));

		if (reportsTableComponent != null)
			reportsTableComponent.setCaption(getLocalizedString("REPORTS_CAPTION"));

		if (gradeTextField != null)
			gradeTextField.setCaption(getLocalizedString("GRADE_CAPTION"));
		
		if (passedCheckBox != null)
			passedCheckBox.setCaption(getLocalizedString("PASSED_CAPTION"));
	}

	@Override
	public void setReadOnly(boolean readOnly)
	{
		super.setReadOnly(readOnly);
		
		form.setReadOnly(readOnly);
	}
	
}