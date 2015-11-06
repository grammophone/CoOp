package softeng.coop.ui.composites;

import softeng.coop.dataaccess.Job;
import softeng.coop.ui.ICoopContext;
import softeng.coop.ui.data.DataItem;
import softeng.coop.ui.presenters.JobDetailsComponentPresenter;
import softeng.coop.ui.viewdefinitions.IJobDetailsComponentView;
import softeng.coop.ui.viewdefinitions.IOkCancelView;
import softeng.coop.ui.viewdefinitions.viewmodels.OkCancelViewModel;
import softeng.ui.vaadin.mvp.IView;
import softeng.ui.vaadin.mvp.Presenter;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

public class JobDetailsComponent 
	extends CoopComponent<DataItem<Job>> 
	implements IJobDetailsComponentView
{
	@AutoGenerated
	private VerticalLayout mainLayout;

	@AutoGenerated
	private OkCancelComponent okCancelComponent;

	@AutoGenerated
	private JobComponent jobComponent;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private static final long serialVersionUID = 1L;

	@Override
	protected void setupUI() 
	{
		super.setupUI();
		
		this.okCancelComponent.setModel(OkCancelViewModel.Save);
		this.okCancelComponent.dataBind();
	}

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public JobDetailsComponent() 
	{
		buildMainLayout();
		setCompositionRoot(mainLayout);

		okCancelComponent.setVisible(false);
	}

	@Override
	protected Presenter<DataItem<Job>, ICoopContext, ? extends IView<DataItem<Job>, ICoopContext>> 
		supplyPresenter() 
	{
		return new JobDetailsComponentPresenter(this);
	}

	@Override
	public void dataBind() 
	{
		if (this.getModel() != null)
		{
			if (!isReadOnly())
				okCancelComponent.setVisible(true);
			else
				okCancelComponent.setVisible(false);
		}
		else
		{
			okCancelComponent.setVisible(false);
		}

		jobComponent.setModel(getModel());
		jobComponent.dataBind();
	}

	@Override
	public void setReadOnly(boolean readOnly) 
	{
		super.setReadOnly(readOnly);
		
		jobComponent.setReadOnly(readOnly);
	}

	@Override
	public boolean isDataValid() 
	{
		return jobComponent.isDataValid();
	}

	@Override
	public void discardChanges() 
	{
		jobComponent.discardChanges();
	}

	@Override
	public void commitChangesToModel() 
	{
		jobComponent.commitChangesToModel();
	}

	@Override
	public IOkCancelView getOkCancelView() 
	{
		return this.okCancelComponent;
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
		
		// jobComponent
		jobComponent = new JobComponent();
		jobComponent.setImmediate(false);
		jobComponent.setWidth("100.0%");
		jobComponent.setHeight("-1px");
		mainLayout.addComponent(jobComponent);
		
		// okCancelComponent
		okCancelComponent = new OkCancelComponent();
		okCancelComponent.setImmediate(false);
		okCancelComponent.setWidth("-1px");
		okCancelComponent.setHeight("-1px");
		mainLayout.addComponent(okCancelComponent);
		mainLayout.setComponentAlignment(okCancelComponent, new Alignment(10));
		
		return mainLayout;
	}

}