package softeng.coop.ui.composites;

import java.util.Locale;

import softeng.coop.business.Session;
import softeng.coop.ui.ICoopContext;
import softeng.coop.ui.data.DataItem;
import softeng.coop.ui.dialogs.ConfirmationDialog;
import softeng.coop.ui.viewdefinitions.IHierarchyView;
import softeng.coop.ui.viewdefinitions.IModalView;
import softeng.coop.ui.viewdefinitions.IOkCancelView;
import softeng.coop.ui.viewdefinitions.viewmodels.OkCancelViewModel;
import softeng.ui.vaadin.data.HierarchicalBeanItemContainer;
import softeng.ui.vaadin.mvp.EventSubscription;
import softeng.ui.vaadin.mvp.IListener;
import softeng.ui.vaadin.mvp.IViewListener;
import softeng.ui.vaadin.mvp.ModelEvent;
import softeng.ui.vaadin.mvp.ViewEvent;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

/**
 * Component to present and edit hierarchical data.
 * @param <M> The type of items shown in the tree.
 */
public abstract class HierarchyComponent<M> 
extends CoopComponent<HierarchicalBeanItemContainer<M>>
implements IHierarchyView<M>
{
	@AutoGenerated
	private VerticalLayout mainLayout;

	@AutoGenerated
	private HorizontalLayout buttonsLayout;

	@AutoGenerated
	private Button deleteButton;

	@AutoGenerated
	private Button editButton;

	@AutoGenerated
	private Button addButton;

	@AutoGenerated
	private Panel treePanel;

	@AutoGenerated
	private VerticalLayout panelVerticalLayout;

	@AutoGenerated
	private Tree tree;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private static final long serialVersionUID = 1L;

	private String itemCaptionId;

	private EventSubscription<ModelEvent<M>, IListener<ModelEvent<M>>> addingSubscription =
		new EventSubscription<ModelEvent<M>, IListener<ModelEvent<M>>>();
	
	private EventSubscription<ModelEvent<M>, IListener<ModelEvent<M>>> editingSubscription = 
		new EventSubscription<ModelEvent<M>, IListener<ModelEvent<M>>>(); 

	private EventSubscription<ModelEvent<M>, IListener<ModelEvent<M>>> deletingSubscription = 
		new EventSubscription<ModelEvent<M>, IListener<ModelEvent<M>>>();
	
	private EventSubscription<ModelEvent<M>, IListener<ModelEvent<M>>> selectedChangeSubscription =
		new EventSubscription<ModelEvent<M>, IListener<ModelEvent<M>>>();
	
	/**
	 * Optionally, show a form for a new added item. 
	 * The new item as specified by createNewElement
	 * is passed as the model of the form.
	 * Else, if returning null, no form will be showed
	 * and the item specified by createNewElement
	 * will be added as-is. 
	 */
	protected abstract IModalView<BeanItem<M>> showAddForm(BeanItem<M> item);
	
	/**
	 * Optionally, show a form to edit the selected item. 
	 * The selected item
	 * is passed as the model of the form.
	 * Else, if returning null, no form will be showed
	 * no edit will take place.
	 */
	protected abstract IModalView<BeanItem<M>> showEditForm(BeanItem<M> item);

	/**
	 * Create a blank new element.
	 */
	protected abstract M createNewElement();

	/**
	 * Create a bean item for an element.
	 */
	protected BeanItem<M> createItem(M obj)
	{
		if (obj != null)
		{
			if (getContext() != null)
			{
				Session session = getContext().getSession();
				
				if (session != null)
				{
					return new DataItem<M>(obj, session.getBaseManager());
				}
			}
			return new BeanItem<M>(obj);
		}
		else
		{
			return null;
		}
	}

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public HierarchyComponent()
	{
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	@Override
	public void addAddingListener(IListener<ModelEvent<M>> listener)
	{
		addingSubscription.add(listener);
	}

	@Override
	public void removeAddingListener(IListener<ModelEvent<M>> listener)
	{
		addingSubscription.remove(listener);
	}

	@Override
	public void addEditingListener(IListener<ModelEvent<M>> listener)
	{
		editingSubscription.add(listener);
	}

	@Override
	public void removeEditingListener(IListener<ModelEvent<M>> listener)
	{
		editingSubscription.remove(listener);
	}

	@Override
	public void addDeletingListener(IListener<ModelEvent<M>> listener)
	{
		deletingSubscription.add(listener);
	}

	@Override
	public void removeDeletingListener(IListener<ModelEvent<M>> listener)
	{
		deletingSubscription.remove(listener);
	}

	@Override
	public void addSelectedChangeListener(IListener<ModelEvent<M>> listener)
	{
		selectedChangeSubscription.add(listener);
	}

	@Override
	public void removeSelectedChangeListener(IListener<ModelEvent<M>> listener)
	{
		selectedChangeSubscription.remove(listener);
	}

	@SuppressWarnings("unchecked")
	@Override
	public M getSelectedValue()
	{
		return (M)tree.getValue();
	}

	@Override
	public void setSelectedValue(M value)
	{
		tree.setValue(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BeanItem<M> getSelectedItem()
	{
		return (BeanItem<M>)getModel().getItem(getSelectedValue());
	}

	@Override
	public boolean isEditVisible()
	{
		return editButton.isVisible();
	}

	@Override
	public void setEditVisible(boolean visible)
	{
		editButton.setVisible(visible);
	}

	@Override
	public boolean isAddVisible()
	{
		return addButton.isVisible();
	}

	@Override
	public void setAddVisible(boolean visible)
	{
		addButton.setVisible(visible);
	}

	@Override
	public boolean isDeleteVisible()
	{
		return deleteButton.isVisible();
	}

	@Override
	public void setDeleteVisible(boolean visible)
	{
		deleteButton.setVisible(visible);
	}

	@SuppressWarnings("serial")
	@Override
	protected void setupUI()
	{
		super.setupUI();
		
		addButton.setCaption(null);
		addButton.setWidth("-1px");
		addButton.setIcon(new ThemeResource("../images/actions/add.png"));
		
		editButton.setCaption(null);
		editButton.setWidth("-1px");
		editButton.setIcon(new ThemeResource("../images/actions/edit.png"));
		
		deleteButton.setCaption(null);
		deleteButton.setWidth("-1px");
		deleteButton.setIcon(new ThemeResource("../images/actions/trash.png"));
		
		tree.setImmediate(true);
		tree.setSelectable(true);
		
		tree.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				onSelectedChanged();
			}
		});
		
		addButton.addListener(new Button.ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				onAdd();
			}
		});
		
		editButton.addListener(new Button.ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				onEdit();
			}
		});
		
		deleteButton.addListener(new Button.ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				onDelete();
			}
		});
		
	}

	protected void onDelete()
	{
		final HierarchicalBeanItemContainer<M> container = getModel();
		
		if (container == null) return;
		
		BeanItem<M> selectedItem = this.getSelectedItem();
		
		if (selectedItem == null) return;
		
		String confirmationCaption = null;
		String confirmationDescription = null;
		
		if (this.getLocale().getLanguage().equals("el"))
		{
			confirmationCaption = "�����������";
			confirmationDescription = "������ �� ��������� �� ���������� �����������";
		}
		else
		{
			confirmationCaption = "Confirmation";
			confirmationDescription = "Are you sure to delete the selected object?";
		}
		
		ConfirmationDialog dialog = 
			new ConfirmationDialog(
					confirmationCaption, 
					confirmationDescription);
		
		dialog.getOkCancelView().addOkListener(new IViewListener<OkCancelViewModel, ICoopContext, IOkCancelView>()
		{
			@Override
			public void onEvent(ViewEvent<OkCancelViewModel, ICoopContext, IOkCancelView> event)
			{
				onDeleteConfirmation();
			}
		});
		
		dialog.getOkCancelView().addOkFailedListener(new IListener<RuntimeException>()
		{
			@Override
			public void onEvent(RuntimeException event)
			{
				throw event;
			}
		});
		
		this.getWindow().addWindow(dialog);
		
	}

	protected void onDeleteConfirmation()
	{
		final HierarchicalBeanItemContainer<M> container = getModel();
		
		if (container == null) return;
		
		BeanItem<M> selectedItem = this.getSelectedItem();
		
		deletingSubscription.fire(new ModelEvent<M>(selectedItem.getBean()));

		container.removeItem(selectedItem.getBean());
		
		tree.setValue(null);
	}

	protected void onSelectedChanged()
	{
		ModelEvent<M> event = new ModelEvent<M>(getSelectedValue());
		
		selectedChangeSubscription.fire(event);
	}

	protected void onAdd()
	{
		final HierarchicalBeanItemContainer<M> container = getModel();
		
		if (container == null) return;
		
		final M newElement = this.createNewElement();
		
		final BeanItem<M> newItem = createItem(newElement);
		
		final IModalView<BeanItem<M>> form = this.showAddForm(newItem);
		
		final M selectedElement = getSelectedValue();
		
		if (form != null)
		{
			form.getOkCancelView().addOkListener(new IViewListener<OkCancelViewModel, ICoopContext, IOkCancelView>()
			{
				@Override
				public void onEvent(ViewEvent<OkCancelViewModel, ICoopContext, IOkCancelView> event)
				{
					if (!form.isDataValid() || form.getModel() == null) return;
					
					BeanItem<M> editedItem = form.getModel();
					
					M editedElement = editedItem.getBean();
					
					ModelEvent<M> modelEvent = new ModelEvent<M>(editedElement);
					
					addingSubscription.fire(modelEvent);
	
					container.addItem(selectedElement, editedElement);
					
					tree.setValue(editedElement);
					
					if (selectedElement != null)
						tree.expandItem(selectedElement);
				}
			});
		}
		else
		{
			if (newElement == null) return;
			
			// If no add form was provided, add the new item directly, with no edit dialog.
			ModelEvent<M> modelEvent = new ModelEvent<M>(newElement);
			
			addingSubscription.fire(modelEvent);
			
			container.addItem(selectedElement, newElement);
			
			tree.setValue(newElement);
		}
	}
	
	protected void onEdit()
	{
		final HierarchicalBeanItemContainer<M> container = getModel();
		
		if (container == null) return;
		
		final BeanItem<M> selectedItem = getSelectedItem();
		
		if (selectedItem == null) return;
		
		final IModalView<BeanItem<M>> form = this.showEditForm(selectedItem);
		
		if (form == null) return;
		
		form.getOkCancelView().addOkListener(new IViewListener<OkCancelViewModel, ICoopContext, IOkCancelView>()
		{
			@Override
			public void onEvent(ViewEvent<OkCancelViewModel, ICoopContext, IOkCancelView> event)
			{
				if (!form.isDataValid()) return;
				
				ModelEvent<M> modelEvent = new ModelEvent<M>(selectedItem.getBean());

				editingSubscription.fire(modelEvent);
			}
		});
	}

	@Override
	public void dataBind()
	{
		tree.setItemCaptionMode(Tree.ITEM_CAPTION_MODE_PROPERTY);
		tree.setItemCaptionPropertyId(this.itemCaptionId);
		tree.setContainerDataSource(this.getModel());
	}

	@Override
	public boolean isReadOnly()
	{
		return super.isReadOnly();
	}

	@Override
	public void setReadOnly(boolean readOnly)
	{
		super.setReadOnly(readOnly);
		
		addButton.setReadOnly(readOnly);
		editButton.setReadOnly(readOnly);
		deleteButton.setReadOnly(readOnly);
	}

	@Override
	protected void setupLocalizedCaptions(Locale locale)
	{
		super.setupLocalizedCaptions(locale);
		
		updateButtonCaptions();
	}
	
	private void updateButtonCaptions()
	{
		this.addButton.setDescription(getContext().getLocalizedString("ADD_CAPTION"));
		this.editButton.setDescription(getContext().getLocalizedString("EDIT_CAPTION"));
		this.deleteButton.setDescription(getContext().getLocalizedString("DELETE_CAPTION"));
	}

	/**
	 * The item id of the caption displayed for each element of type M.
	 */
	public String getItemCaptionId()
	{
		return itemCaptionId;
	}

	/**
	 * The item id of the caption displayed for each element of type M.
	 */
	void setItemCaptionId(String itemCaptionId)
	{
		if (itemCaptionId == null) 
			throw new IllegalArgumentException("Argument 'itemCaptionId' must not be null.");
		
		this.itemCaptionId = itemCaptionId;
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
		mainLayout.setSpacing(true);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// treePanel
		treePanel = buildTreePanel();
		mainLayout.addComponent(treePanel);
		mainLayout.setExpandRatio(treePanel, 1.0f);
		
		// buttonsLayout
		buttonsLayout = buildButtonsLayout();
		mainLayout.addComponent(buttonsLayout);
		mainLayout.setComponentAlignment(buttonsLayout, new Alignment(6));
		
		return mainLayout;
	}

	@AutoGenerated
	private Panel buildTreePanel()
	{
		// common part: create layout
		treePanel = new Panel();
		treePanel.setImmediate(false);
		treePanel.setWidth("100.0%");
		treePanel.setHeight("100.0%");
		
		// panelVerticalLayout
		panelVerticalLayout = buildPanelVerticalLayout();
		treePanel.setContent(panelVerticalLayout);
		
		return treePanel;
	}

	@AutoGenerated
	private VerticalLayout buildPanelVerticalLayout()
	{
		// common part: create layout
		panelVerticalLayout = new VerticalLayout();
		panelVerticalLayout.setImmediate(false);
		panelVerticalLayout.setWidth("100.0%");
		panelVerticalLayout.setHeight("100.0%");
		panelVerticalLayout.setMargin(false);
		
		// tree
		tree = new Tree();
		tree.setImmediate(false);
		tree.setWidth("100.0%");
		tree.setHeight("100.0%");
		panelVerticalLayout.addComponent(tree);
		
		return panelVerticalLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildButtonsLayout()
	{
		// common part: create layout
		buttonsLayout = new HorizontalLayout();
		buttonsLayout.setImmediate(false);
		buttonsLayout.setWidth("-1px");
		buttonsLayout.setHeight("-1px");
		buttonsLayout.setMargin(false);
		buttonsLayout.setSpacing(true);
		
		// addButton
		addButton = new Button();
		addButton.setCaption("Add...");
		addButton.setImmediate(true);
		addButton.setWidth("100px");
		addButton.setHeight("-1px");
		buttonsLayout.addComponent(addButton);
		
		// editButton
		editButton = new Button();
		editButton.setCaption("Edit...");
		editButton.setImmediate(true);
		editButton.setWidth("100px");
		editButton.setHeight("-1px");
		buttonsLayout.addComponent(editButton);
		
		// deleteButton
		deleteButton = new Button();
		deleteButton.setCaption("Delete...");
		deleteButton.setImmediate(true);
		deleteButton.setWidth("100px");
		deleteButton.setHeight("-1px");
		buttonsLayout.addComponent(deleteButton);
		
		return buttonsLayout;
	}
	
	/**
	 * If true, the nodes of the tree can be drag sources and can 
	 * initiate a drag-drop operation. Default is false;
	 */
	public boolean isAllowedAsDragSource()
	{
		return tree.getDragMode() == TreeDragMode.NODE;
	}
	
	/**
	 * If true, the nodes of the tree can be drag sources and can 
	 * initiate a drag-drop operation. Default is false;
	 */
	public void setAllowedAsDragSource(boolean allowedAsDragSource)
	{
		if (allowedAsDragSource)
			tree.setDragMode(TreeDragMode.NODE);
		else
			tree.setDragMode(TreeDragMode.NONE);
	}

	@Override
	public TreeDragMode getDragMode()
	{
		return tree.getDragMode();
	}
	
	@Override
	public void setDragMode(TreeDragMode dragMode)
	{
		tree.setDragMode(dragMode);
	}

	@Override
	public DropHandler getDropHandler()
	{
		return tree.getDropHandler();
	}
	
	@Override
	public void setDropHandler(DropHandler dropHandler)
	{
		tree.setDropHandler(dropHandler);
	}
	
	@Override
	public void expand(M element)
	{
		tree.expandItem(element);
	}
}