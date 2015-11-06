package softeng.coop.ui.composites;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Vector;

import softeng.coop.dataaccess.CoOp;
import softeng.coop.dataaccess.FinancialSource;
import softeng.coop.dataaccess.Payment;
import softeng.coop.dataaccess.PaymentState;
import softeng.coop.dataaccess.PaymentType;
import softeng.coop.dataaccess.Registration;
import softeng.coop.dataaccess.Student;
import softeng.coop.ui.EnumComboBox;
import softeng.coop.ui.ICoopContext;
import softeng.coop.ui.composites.PersonDataComponent.ViewMode;
import softeng.coop.ui.composites.TableComponent.DynamicColumnSpecification;
import softeng.coop.ui.dialogs.ConfirmationDialog;
import softeng.coop.ui.forms.FinancialActionViewModelForm;
import softeng.coop.ui.forms.RegistrationForm;
import softeng.coop.ui.presenters.FinancialCardPresenter;
import softeng.coop.ui.viewdefinitions.IFinancialCardView;
import softeng.coop.ui.viewdefinitions.IOkCancelView;
import softeng.coop.ui.viewdefinitions.ITableView;
import softeng.coop.ui.viewdefinitions.viewmodels.CommandExecutionVote;
import softeng.coop.ui.viewdefinitions.viewmodels.FinancialActionType;
import softeng.coop.ui.viewdefinitions.viewmodels.FinancialActionViewModel;
import softeng.coop.ui.viewdefinitions.viewmodels.OkCancelViewModel;
import softeng.ui.vaadin.mvp.EventSubscription;
import softeng.ui.vaadin.mvp.IListener;
import softeng.ui.vaadin.mvp.IView;
import softeng.ui.vaadin.mvp.ModelEvent;
import softeng.ui.vaadin.mvp.Presenter;
import softeng.ui.vaadin.mvp.PropertyChangeEvent;
import softeng.ui.vaadin.mvp.PropertyChangeEventSubscription;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;


@SuppressWarnings("serial")
public class FinancialCardComponent 
extends CoopComponent<BeanItem<FinancialActionViewModel>>
implements IFinancialCardView
{

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private HorizontalLayout unassignedRegistrationsHorizontalLayout;
	@AutoGenerated
	private VerticalLayout unassignedRegistrationDetailsVerticalLayout;
	@AutoGenerated
	private RegistrationForm unassignedRegistrationForm;
	@AutoGenerated
	private PersonDataComponent unassignedPersonDataComponent;
	@AutoGenerated
	private RegistrationsTableComponent unassignedRegistrationsTableComponent;
	@AutoGenerated
	private HorizontalLayout assignedRegistrationsHorizontalLayout;
	@AutoGenerated
	private VerticalLayout assignedRegistrationFormVerticalLayout;
	@AutoGenerated
	private OkCancelComponent okCancelComponent;
	@AutoGenerated
	private RegistrationForm assignedRegistrationForm;
	@AutoGenerated
	private RegistrationsTableComponent assignedRegistrationsTableComponent;
	@AutoGenerated
	private Button executeButton;
	@AutoGenerated
	private HorizontalLayout actionAndFiltersLayout;
	@AutoGenerated
	private FinancialActionViewModelForm actionForm;
	@AutoGenerated
	private FinancialActionViewModelForm filtersForm;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private static Vector<String> actionPropertyIds = createActionPropertyIds();
	
	private static Vector<String> filtersPropertyIds = createFiltersPropertyIds();
	
	private static Vector<String> assignedRegistrationPropertyIds = createAssignedRegistrationPropertyIds();
	
	private static Vector<String> unassignedRegistrationPropertyIds = createUnassignedRegistrationPropertyIds();
	
	private EventSubscription<CommandExecutionVote, IListener<CommandExecutionVote>> executeSubscription =
		new EventSubscription<CommandExecutionVote, IListener<CommandExecutionVote>>();
	
	// Fields for FincancialActionViewModel
	private TextField localDayCompensationTextField = new TextField();
	private TextField remoteDayCompensationTextField = new TextField();
	private TextField offCountryDayCompensationTextField = new TextField();
	private EnumComboBox paymentTypeComboBox = new EnumComboBox(PaymentType.class);
	private EnumComboBox paymentStateComboBox = new EnumComboBox(PaymentState.class);
	private EnumComboBox actionTypeComboBox = new EnumComboBox(FinancialActionType.class);
	private FinancialSourcePickerField financialSourcePickerField = new FinancialSourcePickerField(null);
	private DateField paymentDateField = new DateField();
	
	// Fields for selected assigned Registration.
	private TextField assignedNameTextField;
	private TextField assignedSurnameTextField;
	private TextField assignedSerialNumberTextField;
	private PaymentsTableComponent assignedPaymentsTableComponent;
	private TextField assignedIbanTextField;
	private TextField assignedTaxCodeTextField;
	
	// Fields for selected unassigned Registration.
	private RequirementsCheckListField unassignedRequirementsCheckListField;
	private CheckBox qualifiedForAssignmentCheckBox;
	private CheckBox qualifiedForCompletionCheckBox;
	private TextField unassignedSerialNumberTextField;
	
	private PropertyChangeEventSubscription<FinancialActionViewModel> filtersChangeEventSubscription =
		new PropertyChangeEventSubscription<FinancialActionViewModel>();
	
	private class FinancialSourceSumColumnGenerator 
	implements Table.ColumnGenerator
	{
		private FinancialSource financialSource;
		
		public FinancialSourceSumColumnGenerator(FinancialSource financialSource)
		{
			if (financialSource == null) 
				throw new IllegalArgumentException("Argument 'financialSource' must not be null.");
			
			this.financialSource = financialSource;
		}
		
		@Override
		public Object generateCell(Table source, Object itemId, Object columnId)
		{
			BigDecimal accumulator = new BigDecimal(0.0);
			
			if (getModel() == null) return accumulator;
			
			FinancialActionViewModel actionViewModel = getModel().getBean();
			
			Registration registration = (Registration)itemId;
			
			for (Payment payment : registration.getPayments())
			{
				if (payment.getState() != actionViewModel.getPaymentState() ||
						payment.getType() != actionViewModel.getPaymentType() ||
						payment.getSource() != financialSource)
				{
					continue;
				}
				
				accumulator = accumulator.add(payment.getAmount());
			}
			
			return accumulator;
		}
	}
	
	private class FinancialSourceCaptionGenerator
	implements TableComponent.CaptionGenerator
	{
		private FinancialSource financialSource;
		
		public FinancialSourceCaptionGenerator(FinancialSource financialSource)
		{
			if (financialSource == null) 
				throw new IllegalArgumentException("Argument 'financialSource' must not be null.");
			
			this.financialSource = financialSource;
		}
		
		@Override
		public String getCaption(ICoopContext context)
		{
			return financialSource.getName();
		}
		
	}

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public FinancialCardComponent()
	{
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// Fields for FincancialActionViewModel
		localDayCompensationTextField.setWidth("100%");
		localDayCompensationTextField.setNullRepresentation("");
		localDayCompensationTextField.setNullSettingAllowed(true);
		localDayCompensationTextField.setEnabled(false);
		
		remoteDayCompensationTextField.setWidth("100%");
		remoteDayCompensationTextField.setNullRepresentation("");
		remoteDayCompensationTextField.setNullSettingAllowed(true);
		remoteDayCompensationTextField.setEnabled(false);
		
		offCountryDayCompensationTextField.setWidth("100%");
		offCountryDayCompensationTextField.setNullRepresentation("");
		offCountryDayCompensationTextField.setNullSettingAllowed(true);
		offCountryDayCompensationTextField.setEnabled(false);
		
		paymentTypeComboBox.setWidth("100%");
		paymentTypeComboBox.setNullSelectionAllowed(false);
		
		paymentStateComboBox.setWidth("100%");
		paymentStateComboBox.setNullSelectionAllowed(false);
		
		actionTypeComboBox.setWidth("100%");
		actionTypeComboBox.setNullSelectionAllowed(false);
		
		financialSourcePickerField.setWidth("100%");
		financialSourcePickerField.setClearAllowed(false);
		
		paymentDateField.setWidth("100%");
		paymentDateField.setResolution(DateField.RESOLUTION_SEC);
		paymentDateField.setEnabled(false);

		// Events setup.
		okCancelComponent.setVisible(false);
		
		assignedRegistrationsTableComponent.addSelectedChangeListener(new IListener<ModelEvent<Registration>>()
		{
			@Override
			public void onEvent(ModelEvent<Registration> event)
			{
				onSelectedAssignedRegistrationChanged();
			}
		});
		
		unassignedRegistrationsTableComponent.addSelectedChangeListener(new IListener<ModelEvent<Registration>>()
		{
			@Override
			public void onEvent(ModelEvent<Registration> event)
			{
				onSelectedUnassignedRegistrationChanged();
			}
		});
		
		executeButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				onExecute();
			}
		});
		
		actionTypeComboBox.addListener(new Listener()
		{
			@Override
			public void componentEvent(Event event)
			{
				onActionTypeChanged();
			}
		});
		
		filtersChangeEventSubscription.add(new IListener<PropertyChangeEvent<FinancialActionViewModel>>()
		{
			@Override
			public void onEvent(PropertyChangeEvent<FinancialActionViewModel> event)
			{
				onFiltersChange();
			}
		});
		
	}

	protected void onSelectedUnassignedRegistrationChanged()
	{
		BeanItem<Registration> registrationItem = unassignedRegistrationsTableComponent.getSelectedItem();
		
		BeanItem<Student> studentItem;
		
		if (registrationItem != null)
			studentItem = new BeanItem<Student>(registrationItem.getBean().getStudent());
		else
			studentItem = null;
		
		unassignedPersonDataComponent.setModel(studentItem);
		
		unassignedPersonDataComponent.dataBind();
		
		unassignedRegistrationForm.setItemDataSource(registrationItem, unassignedRegistrationPropertyIds);
	}

	protected void onFiltersChange()
	{
		assignedRegistrationsTableComponent.refreshRowCache();
	}

	protected void onActionTypeChanged()
	{
		FinancialActionType actionType = (FinancialActionType)actionTypeComboBox.getValue();
		
		boolean enableCompensationFields = (actionType == FinancialActionType.Add);
		
		localDayCompensationTextField.setEnabled(enableCompensationFields);
		remoteDayCompensationTextField.setEnabled(enableCompensationFields);
		offCountryDayCompensationTextField.setEnabled(enableCompensationFields);
		paymentDateField.setEnabled(enableCompensationFields);
	}

	private static Vector<String> createAssignedRegistrationPropertyIds()
	{
		Vector<String> list = new Vector<String>();
		
		list.add("student.surname");
		list.add("student.name");
		list.add("student.serialNumber");
		list.add("student.iban");
		list.add("student.taxId");
		list.add("payments");
		
		return list;
	}

	private static Vector<String> createFiltersPropertyIds()
	{
		Vector<String> list = new Vector<String>();
		
		list.add("paymentType");
		list.add("paymentState");
		
		return list;
	}

	private static Vector<String> createActionPropertyIds()
	{
		Vector<String> list = new Vector<String>();
		
		list.add("actionType");
		list.add("financialSource");
		list.add("localDayCompensation");
		list.add("remoteDayCompensation");
		list.add("offCountryDayCompensation");
		list.add("paymentDate");
		
		return list;
	}
	
	private static Vector<String> createUnassignedRegistrationPropertyIds()
	{
		Vector<String> list = new Vector<String>();

		list.add("student.serialNumber");
		list.add("meetsRequirements");
		list.add("qualifiedForAssigmnent");
		list.add("qualifiedForCompletion");
		
		return list;
	}

	@Override
	public boolean isDataValid()
	{
		return assignedRegistrationForm.isValid();
	}

	@Override
	public void discardChanges()
	{
		assignedRegistrationForm.discard();
	}

	@Override
	public void commitChangesToModel()
	{
		assignedRegistrationForm.commit();
	}

	@Override
	public ITableView<CoOp, Registration> getAssignedRegistrationsTableView()
	{
		return assignedRegistrationsTableComponent;
	}

	@Override
	public ITableView<CoOp, Registration> getUnassignedRegistrationsTableView()
	{
		return unassignedRegistrationsTableComponent;
	}

	@Override
	public void addExecuteActionListener(IListener<CommandExecutionVote> listener)
	{
		executeSubscription.add(listener);
	}

	@Override
	public void removeExecuteActionListener(IListener<CommandExecutionVote> listener)
	{
		executeSubscription.remove(listener);
	}

	@Override
	protected Presenter<BeanItem<FinancialActionViewModel>, ICoopContext, ? extends IView<BeanItem<FinancialActionViewModel>, ICoopContext>> supplyPresenter()
	{
		return new FinancialCardPresenter(this);
	}

	@Override
	public void dataBind()
	{
		actionForm.setItemDataSource(getModel(), actionPropertyIds);
		
		filtersForm.setItemDataSource(getModel(), filtersPropertyIds);
	}
	
	@Override
	public void setModel(BeanItem<FinancialActionViewModel> model)
	{
		if (getModel() != null)
			filtersChangeEventSubscription.stopListeningTo(getModel());
		
		super.setModel(model);
		
		if (model != null)
			filtersChangeEventSubscription.startListeningTo(model);
	}

	@Override
	protected void setupUI()
	{
		super.setupUI();
		
		okCancelComponent.setModel(OkCancelViewModel.Save);
		
		filtersForm.setImmediate(true);
		filtersForm.setWriteThrough(true);
		
		filtersForm.setFormFieldFactory(new FormFieldFactory()
		{
			@Override
			public Field createField(Item item, Object propertyId, Component uiContext)
			{
				if (propertyId.equals("paymentState"))
				{
					return paymentStateComboBox;
				}
				else if (propertyId.equals("paymentType"))
				{
					return paymentTypeComboBox;
				}
				
				return null;
			}
		});
		
		actionForm.setImmediate(true);
		actionForm.setWriteThrough(false);
		
		actionForm.setFormFieldFactory(new FormFieldFactory()
		{
			@Override
			public Field createField(Item item, Object propertyId, Component uiContext)
			{
				if (propertyId.equals("localDayCompensation"))
				{
					return localDayCompensationTextField;
				}
				else if (propertyId.equals("remoteDayCompensation"))
				{
					return remoteDayCompensationTextField;
				}
				else if (propertyId.equals("offCountryDayCompensation"))
				{
					return offCountryDayCompensationTextField;
				}
				else if (propertyId.equals("financialSource"))
				{
					financialSourcePickerField.setCoop(getContext().getSelectedCoop());
					return financialSourcePickerField;
				}
				else if (propertyId.equals("actionType"))
				{
					return actionTypeComboBox;
				}
				else if (propertyId.equals("paymentDate"))
				{
					return paymentDateField;
				}

				return null;
			}
		});
		
		assignedRegistrationForm.setImmediate(true);
		assignedRegistrationForm.setWriteThrough(false);
		
		assignedRegistrationForm.setFormFieldFactory(new FormFieldFactory()
		{
			@Override
			public Field createField(Item item, Object propertyId, Component uiContext)
			{
				if (propertyId.equals("student.name"))
				{
					assignedNameTextField = new TextField();
					assignedNameTextField.setCaption(getLocalizedString("NAME_CAPTION"));
					assignedNameTextField.setWidth("100%");
					assignedNameTextField.setReadOnly(true);
					assignedNameTextField.setNullRepresentation("");
					
					return assignedNameTextField;
				}
				else if (propertyId.equals("student.surname"))
				{
					assignedSurnameTextField = new TextField();
					assignedSurnameTextField.setCaption(getLocalizedString("SURNAME_CAPTION"));
					assignedSurnameTextField.setWidth("100%");
					assignedSurnameTextField.setReadOnly(true);
					assignedSurnameTextField.setNullRepresentation("");
					
					return assignedSurnameTextField;
				}
				else if (propertyId.equals("student.serialNumber"))
				{
					assignedSerialNumberTextField = new TextField();
					assignedSerialNumberTextField.setCaption(getLocalizedString("SERIAL_NUMBER_CAPTION"));
					assignedSerialNumberTextField.setWidth("100%");
					assignedSerialNumberTextField.setReadOnly(true);
					assignedSerialNumberTextField.setNullRepresentation("");
					
					return assignedSerialNumberTextField;
				}
				else if (propertyId.equals("payments"))
				{
					assignedPaymentsTableComponent = new PaymentsTableComponent();
					assignedPaymentsTableComponent.setCaption(getLocalizedString("PAYMENTS_CAPTION"));
					assignedPaymentsTableComponent.setWidth("100%");
					
					assignedPaymentsTableComponent.setParentModel(assignedRegistrationsTableComponent.getSelectedValue());

					return assignedPaymentsTableComponent;
				}
				else if (propertyId.equals("student.taxId"))
				{
					assignedTaxCodeTextField = new TextField();
					assignedTaxCodeTextField.setCaption(getLocalizedString("TAX_CODE_CAPTION"));
					assignedTaxCodeTextField.setWidth("100%");
					assignedTaxCodeTextField.setReadOnly(true);
					assignedTaxCodeTextField.setNullRepresentation("");
					
					return assignedTaxCodeTextField;
				}
				else if (propertyId.equals("student.iban"))
				{
					assignedIbanTextField = new TextField();
					assignedIbanTextField.setCaption(getLocalizedString("IBAN_CAPTION"));
					assignedIbanTextField.setWidth("100%");
					assignedIbanTextField.setReadOnly(true);
					assignedIbanTextField.setNullRepresentation("");
					
					return assignedIbanTextField;
				}

				return null;
			}
		});
		
		unassignedRegistrationForm.setReadOnly(true);
		
		unassignedRegistrationForm.setFormFieldFactory(new FormFieldFactory()
		{
			
			@Override
			public Field createField(Item item, Object propertyId, Component uiContext)
			{
				if (propertyId.equals("qualifiedForAssigmnent"))
				{
					qualifiedForAssignmentCheckBox = new CheckBox();
					qualifiedForAssignmentCheckBox.setCaption(getLocalizedString("QUALIFIED_FOR_ASSIGNMENT_CAPTION"));
					qualifiedForAssignmentCheckBox.setWidth("100%");
					
					return qualifiedForAssignmentCheckBox;
				}
				else if (propertyId.equals("qualifiedForCompletion"))
				{
					qualifiedForCompletionCheckBox = new CheckBox();
					qualifiedForCompletionCheckBox.setCaption(getLocalizedString("QUALIFIED_FOR_COMPLETION_CAPTION"));
					qualifiedForCompletionCheckBox.setWidth("100%");
					
					return qualifiedForCompletionCheckBox;
				}
				else if (propertyId.equals("meetsRequirements"))
				{
					CoOp selectedCoop = getContext().getSelectedCoop();
					
					if (selectedCoop == null) return null;

					unassignedRequirementsCheckListField = new RequirementsCheckListField();
					unassignedRequirementsCheckListField.setCaption(getLocalizedString("REQUIREMENTS_CAPTION"));
					unassignedRequirementsCheckListField.setWidth("100%");
					
					unassignedRequirementsCheckListField.setAvailableElements(selectedCoop.getRequirements());
					
					unassignedRequirementsCheckListField.setRegistration(
							getUnassignedRegistrationsTableView().getSelectedValue());
					
					return unassignedRequirementsCheckListField;
				}
				else if (propertyId.equals("student.serialNumber"))
				{
					unassignedSerialNumberTextField = new TextField();
					
					unassignedSerialNumberTextField.setWidth("100%");
					unassignedSerialNumberTextField.setCaption(getLocalizedString("SERIAL_NUMBER_CAPTION"));
					unassignedSerialNumberTextField.setNullRepresentation("");
					unassignedSerialNumberTextField.setNullSettingAllowed(true);
					
					return unassignedSerialNumberTextField;
				}
				
				return null;
			}
		});
		
		assignedRegistrationsTableComponent.setImmediate(true);
		
		unassignedRegistrationsTableComponent.setImmediate(true);
		
		unassignedPersonDataComponent.setReadOnly(true);
		unassignedPersonDataComponent.setMode(ViewMode.Compact);
		
	}

	@Override
	protected void setupLocalizedCaptions(Locale locale)
	{
		super.setupLocalizedCaptions(locale);
		
		assignedRegistrationsTableComponent.setCaption(getLocalizedString("ASSIGNED_REGISTRATIONS_CAPTION"));
		
		unassignedRegistrationsTableComponent.setCaption(getLocalizedString("UNASSIGNED_REGISTRATIONS_CAPTION"));

		assignedRegistrationForm.setCaption(getLocalizedString("ASSIGNED_REGISTRATION_FORM_CAPTION"));
		
		localDayCompensationTextField.setCaption(getLocalizedString("LOCAL_DAY_COMPENSATION_CAPTION"));
		remoteDayCompensationTextField.setCaption(getLocalizedString("REMOTE_DAY_COMPENSATION_CAPTION"));
		offCountryDayCompensationTextField.setCaption(getLocalizedString("OFF_COUNTRY_DAY_COMPENSATION_CAPTION"));
		paymentTypeComboBox.setCaption(getLocalizedString("PAYMENT_TYPE_CAPTION"));
		financialSourcePickerField.setCaption(getLocalizedString("FINANCIAL_SOURCE_CAPTION"));
		paymentStateComboBox.setCaption(getLocalizedString("PAYMENT_STATE_CAPTION"));
		actionTypeComboBox.setCaption(getLocalizedString("ACTION_TYPE_CAPTION"));
		paymentDateField.setCaption(getLocalizedString("PAYMENT_DATE_CAPTION"));
		paymentDateField.setLocale(locale);
		
		if (assignedNameTextField != null)
			assignedNameTextField.setCaption(getLocalizedString("NAME_CAPTION"));
		
		if (assignedSurnameTextField != null)
			assignedSurnameTextField.setCaption(getLocalizedString("SURNAME_CAPTION"));
		
		if (assignedSerialNumberTextField != null)
			assignedSerialNumberTextField.setCaption(getLocalizedString("SERIAL_NUMBER_CAPTION"));
		
		if (assignedPaymentsTableComponent != null)
			assignedPaymentsTableComponent.setCaption(getLocalizedString("PAYMENTS_CAPTION"));
		
		if (assignedTaxCodeTextField != null)
			assignedTaxCodeTextField.setCaption(getLocalizedString("TAX_CODE_CAPTION"));
		
		if (assignedIbanTextField != null)
			assignedIbanTextField.setCaption(getLocalizedString("IBAN_CAPTION"));
		
		if (unassignedRequirementsCheckListField != null)
			unassignedRequirementsCheckListField.setCaption(getLocalizedString("REQUIREMENTS_CAPTION"));
		
		if (qualifiedForAssignmentCheckBox != null)
			qualifiedForAssignmentCheckBox.setCaption(getLocalizedString("QUALIFIED_FOR_ASSIGNMENT_CAPTION"));
		
		if (qualifiedForCompletionCheckBox != null)
			qualifiedForCompletionCheckBox.setCaption(getLocalizedString("QUALIFIED_FOR_COMPLETION_CAPTION"));
		
		if (unassignedSerialNumberTextField != null)
			unassignedSerialNumberTextField.setCaption(getLocalizedString("SERIAL_NUMBER_CAPTION"));
		
		executeButton.setCaption(getLocalizedString("EXECUTE_CAPTION"));
		executeButton.setDescription(getLocalizedString("EXECUTE_DESCRIPTION"));
		
		actionForm.setCaption(getLocalizedString("ACTION_FORM_CAPTION"));
		
		filtersForm.setCaption(getLocalizedString("FILTER_FORM_CAPTION"));
	}

	protected void onSelectedAssignedRegistrationChanged()
	{
		BeanItem<Registration> selectedItem = 
			assignedRegistrationsTableComponent.getSelectedItem();
		
		assignedRegistrationForm.setItemDataSource(
				selectedItem, 
				assignedRegistrationPropertyIds);
		
		okCancelComponent.setVisible(selectedItem != null);
	}

	protected void onExecute()
	{
		if (getContext().getSelectedCoop() == null) return;
		
		if (!actionForm.isValid() || !filtersForm.isValid())
		{
			getContext().showInvalidDataNotification();
			return;
		}
		
		actionForm.commit();
		
		ConfirmationDialog dialog = 
			new ConfirmationDialog(
					getLocalizedString("CONFIRM_ACTION_CAPTION"), 
					getLocalizedString("CONFIRM_ACTION_DESCRIPTION"));
		
		dialog.setModal(true);
		
		dialog.getOkCancelView().addExecuteListener(new IListener<CommandExecutionVote>()
		{
			@Override
			public void onEvent(CommandExecutionVote event)
			{
				onExecuteConfirmed(event);
			}
		});
		
		getApplication().getMainWindow().addWindow(dialog);
		
	}

	protected void onExecuteConfirmed(CommandExecutionVote event)
	{
		CommandExecutionVote vote = new CommandExecutionVote();
		
		try
		{
			executeSubscription.fire(vote);
			
			if (!vote.isFailed())
			{
				getContext().showNotification(
						getLocalizedString("ACTION_SUCCEEDED_CAPTION"), 
						getLocalizedString("ACTION_SUCCEEDED_DESCRIPTION"), 
						Notification.TYPE_HUMANIZED_MESSAGE);
				
				assignedRegistrationsTableComponent.setSelectedValue(null);
				assignedRegistrationsTableComponent.refreshRowCache();
			}
		}
		catch (RuntimeException ex)
		{
			getContext().getSession().revertOutstanding();
		}
	}

	@Override
	public IOkCancelView getOkCancelView()
	{
		return okCancelComponent;
	}

	@Override
	public void setFinancialSources(CoOp coop, Collection<FinancialSource> financialSources)
	{
		if (coop == null) 
			throw new IllegalArgumentException("Argument 'coop' must not be null.");
		if (financialSources == null) 
			throw new IllegalArgumentException("Argument 'financialSources' must not be null.");
		
		financialSourcePickerField.setCoop(coop);
		
		ArrayList<DynamicColumnSpecification> columnSpecifications = 
			new ArrayList<DynamicColumnSpecification>(financialSources.size());
		
		for (FinancialSource financialSource : financialSources)
		{
			columnSpecifications.add(
					new DynamicColumnSpecification(
							financialSource.toString(), 
							new FinancialSourceSumColumnGenerator(financialSource), 
							new FinancialSourceCaptionGenerator(financialSource)));
		}
		
		assignedRegistrationsTableComponent.setDynamicColumnSpecifications(columnSpecifications);
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout()
	{
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(false);
		mainLayout.setSpacing(true);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("-1px");
		
		// actionAndFiltersLayout
		actionAndFiltersLayout = buildActionAndFiltersLayout();
		mainLayout.addComponent(actionAndFiltersLayout);
		
		// executeButton
		executeButton = new Button();
		executeButton.setCaption("Execute");
		executeButton.setImmediate(true);
		executeButton.setWidth("120px");
		executeButton.setHeight("-1px");
		mainLayout.addComponent(executeButton);
		mainLayout.setComponentAlignment(executeButton, new Alignment(6));
		
		// assignedRegistrationsHorizontalLayout
		assignedRegistrationsHorizontalLayout = buildAssignedRegistrationsHorizontalLayout();
		mainLayout.addComponent(assignedRegistrationsHorizontalLayout);
		
		// unassignedRegistrationsHorizontalLayout
		unassignedRegistrationsHorizontalLayout = buildUnassignedRegistrationsHorizontalLayout();
		mainLayout.addComponent(unassignedRegistrationsHorizontalLayout);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildActionAndFiltersLayout()
	{
		// common part: create layout
		actionAndFiltersLayout = new HorizontalLayout();
		actionAndFiltersLayout.setImmediate(false);
		actionAndFiltersLayout.setWidth("100.0%");
		actionAndFiltersLayout.setHeight("-1px");
		actionAndFiltersLayout.setMargin(false);
		actionAndFiltersLayout.setSpacing(true);
		
		// filtersForm
		filtersForm = new FinancialActionViewModelForm();
		filtersForm.setCaption("Filters Form");
		filtersForm.setImmediate(false);
		filtersForm.setWidth("100.0%");
		filtersForm.setHeight("-1px");
		actionAndFiltersLayout.addComponent(filtersForm);
		actionAndFiltersLayout.setExpandRatio(filtersForm, 0.5f);
		
		// actionForm
		actionForm = new FinancialActionViewModelForm();
		actionForm.setCaption("Action Form");
		actionForm.setImmediate(false);
		actionForm.setWidth("100.0%");
		actionForm.setHeight("-1px");
		actionAndFiltersLayout.addComponent(actionForm);
		actionAndFiltersLayout.setExpandRatio(actionForm, 0.5f);
		
		return actionAndFiltersLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildAssignedRegistrationsHorizontalLayout()
	{
		// common part: create layout
		assignedRegistrationsHorizontalLayout = new HorizontalLayout();
		assignedRegistrationsHorizontalLayout.setImmediate(false);
		assignedRegistrationsHorizontalLayout.setWidth("100.0%");
		assignedRegistrationsHorizontalLayout.setHeight("-1px");
		assignedRegistrationsHorizontalLayout.setMargin(false);
		assignedRegistrationsHorizontalLayout.setSpacing(true);
		
		// assignedRegistrationsTableComponent
		assignedRegistrationsTableComponent = new RegistrationsTableComponent();
		assignedRegistrationsTableComponent.setCaption("Registrations Assigned to Jobs");
		assignedRegistrationsTableComponent.setImmediate(false);
		assignedRegistrationsTableComponent.setWidth("100.0%");
		assignedRegistrationsTableComponent.setHeight("460px");
		assignedRegistrationsHorizontalLayout.addComponent(assignedRegistrationsTableComponent);
		assignedRegistrationsHorizontalLayout.setExpandRatio(assignedRegistrationsTableComponent, 0.5f);
		
		// registrationFormVerticalLayout
		assignedRegistrationFormVerticalLayout = buildRegistrationFormVerticalLayout();
		assignedRegistrationsHorizontalLayout.addComponent(assignedRegistrationFormVerticalLayout);
		assignedRegistrationsHorizontalLayout.setExpandRatio(assignedRegistrationFormVerticalLayout, 0.5f);
		
		return assignedRegistrationsHorizontalLayout;
	}

	@AutoGenerated
	private VerticalLayout buildRegistrationFormVerticalLayout()
	{
		// common part: create layout
		assignedRegistrationFormVerticalLayout = new VerticalLayout();
		assignedRegistrationFormVerticalLayout.setImmediate(false);
		assignedRegistrationFormVerticalLayout.setWidth("100.0%");
		assignedRegistrationFormVerticalLayout.setHeight("-1px");
		assignedRegistrationFormVerticalLayout.setMargin(false);
		assignedRegistrationFormVerticalLayout.setSpacing(true);
		
		// registrationForm
		assignedRegistrationForm = new RegistrationForm();
		assignedRegistrationForm.setCaption("Registration's Payments");
		assignedRegistrationForm.setImmediate(false);
		assignedRegistrationForm.setWidth("100.0%");
		assignedRegistrationForm.setHeight("-1px");
		assignedRegistrationFormVerticalLayout.addComponent(assignedRegistrationForm);
		
		// okCancelComponent
		okCancelComponent = new OkCancelComponent();
		okCancelComponent.setImmediate(false);
		okCancelComponent.setWidth("-1px");
		okCancelComponent.setHeight("-1px");
		assignedRegistrationFormVerticalLayout.addComponent(okCancelComponent);
		assignedRegistrationFormVerticalLayout.setComponentAlignment(okCancelComponent, new Alignment(10));
		
		return assignedRegistrationFormVerticalLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildUnassignedRegistrationsHorizontalLayout()
	{
		// common part: create layout
		unassignedRegistrationsHorizontalLayout = new HorizontalLayout();
		unassignedRegistrationsHorizontalLayout.setImmediate(false);
		unassignedRegistrationsHorizontalLayout.setWidth("100.0%");
		unassignedRegistrationsHorizontalLayout.setHeight("-1px");
		unassignedRegistrationsHorizontalLayout.setMargin(false);
		unassignedRegistrationsHorizontalLayout.setSpacing(true);
		
		// unassignedRegistrationsTableComponent
		unassignedRegistrationsTableComponent = new RegistrationsTableComponent();
		unassignedRegistrationsTableComponent.setCaption("Unassigned Registrations");
		unassignedRegistrationsTableComponent.setImmediate(false);
		unassignedRegistrationsTableComponent.setWidth("100.0%");
		unassignedRegistrationsTableComponent.setHeight("-1px");
		unassignedRegistrationsHorizontalLayout.addComponent(unassignedRegistrationsTableComponent);
		unassignedRegistrationsHorizontalLayout.setExpandRatio(unassignedRegistrationsTableComponent, 0.5f);
		
		// unassignedRegistrationDetailsVerticalLayout
		unassignedRegistrationDetailsVerticalLayout = buildUnassignedRegistrationDetailsVerticalLayout();
		unassignedRegistrationsHorizontalLayout.addComponent(unassignedRegistrationDetailsVerticalLayout);
		unassignedRegistrationsHorizontalLayout.setExpandRatio(unassignedRegistrationDetailsVerticalLayout, 0.5f);
		
		return unassignedRegistrationsHorizontalLayout;
	}

	@AutoGenerated
	private VerticalLayout buildUnassignedRegistrationDetailsVerticalLayout()
	{
		// common part: create layout
		unassignedRegistrationDetailsVerticalLayout = new VerticalLayout();
		unassignedRegistrationDetailsVerticalLayout.setImmediate(false);
		unassignedRegistrationDetailsVerticalLayout.setWidth("100.0%");
		unassignedRegistrationDetailsVerticalLayout.setHeight("-1px");
		unassignedRegistrationDetailsVerticalLayout.setMargin(false);
		unassignedRegistrationDetailsVerticalLayout.setSpacing(true);
		
		// unassignedPersonDataComponent
		unassignedPersonDataComponent = new PersonDataComponent();
		unassignedPersonDataComponent.setImmediate(false);
		unassignedPersonDataComponent.setWidth("100.0%");
		unassignedPersonDataComponent.setHeight("-1px");
		unassignedRegistrationDetailsVerticalLayout.addComponent(unassignedPersonDataComponent);
		
		// unassignedRegistrationForm
		unassignedRegistrationForm = new RegistrationForm();
		unassignedRegistrationForm.setImmediate(false);
		unassignedRegistrationForm.setWidth("100.0%");
		unassignedRegistrationForm.setHeight("-1px");
		unassignedRegistrationDetailsVerticalLayout.addComponent(unassignedRegistrationForm);
		
		return unassignedRegistrationDetailsVerticalLayout;
	}

}