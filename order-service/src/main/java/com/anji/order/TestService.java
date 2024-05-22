package com.anji.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestService {


	// public static void main(String[] args) throws Exception {
	// 	System.out.println("Hello Anji, Welcome"); 
		

	// 	for (int i = 0; i < 10; i++) {
	// 		SharedResource sharedResource = new SharedResource();
	// 		sharedResource.method1();
	// 		sharedResource.setCommonMaskableEntity(i+1);
	// 		Thread maskRunnable = new Thread(new MaskRunnable(sharedResource));
	// 		maskRunnable.start(); 
	// 	}
		

	// }
 

}
// class Super{

// 	private Integer commonMaskableEntity = 0;

// 	void method1() throws IOException{
// 		System.out.println("On Super Method1");
// 	}

// 	protected void setCommonMaskableEntity(Integer commonMaskableEntity){
// 		this.commonMaskableEntity = commonMaskableEntity;
// 	}

// 	protected Integer getCommonMaskableEntity(){
// 		return this.commonMaskableEntity;
// 	}
// }
// class Sub extends Super{
	 
// 	void method1() throws IOException{
// 		System.out.println("On Super Method1");
// 	}
// }

// class SharedResource extends Sub{

// }

// class MaskRunnable implements Runnable{

// 	private SharedResource sharedResource = null;

// 	public MaskRunnable(SharedResource sharedResource){
// 		this.sharedResource = sharedResource;
// 	}

// 	@Override
// 	public void run() {
		
// 		System.out.println();
// 		System.out.println();
// 		System.out.println("I am on Thread ::"+sharedResource.getCommonMaskableEntity());
// 		// if(sharedResource.getCommonMaskableEntity() != Integer.valueOf(0)){
// 		// 	throw new UnsupportedOperationException("Unimplemented method 'run'");
// 		// } 
		
// 	}
	
// }

// class Task{
// 	private String id                    ;
// 	private String user_id               ;
// 	private String owner_id              ;
// 	private String account_id            ;
// 	private String display_id            ;
// 	private String status                ;
// 	private String taskable_id           ;
// 	private String taskable_type         ;
// 	private String due_date              ;
// 	private String notify_before         ;
// 	private String deleted               ;
// 	private String title                 ;
// 	private String description           ;
// 	private String created_at            ;
// 	private String updated_at            ;
// 	private String closed_at             ;
// 	private String group_id              ;
// 	private String parent_id             ;
// 	private String root_id               ;
// 	private String start_date            ;
// 	private String dependencies_count    ;
// 	private String taskable_display_id   ;
// 	private String wf_event_id           ;
// 	private String planned_start_date    ;
// 	private String planned_end_date      ;
// 	private String planned_effort        ;
// 	private String planned               ;
// 	private String workspace_id          ;
// 	private String parent_owner_id       ;
// 	private String parent_group_id       ;
// 	private String parent_status_id      ;
// 	private String parent_workspace_id   ;


// 	public void update(){

// 	}

// }

// class TaskModel{

// 	save();
// 	update();
// 	delete();
// 	get();
// }

// class TaskService{

// 	reorder();
//     reformat();
	
// }

// class TaskBackground{


// 	static public void executeTaskOLAUpdate(){

// 		//loop throuh each task and save task
// 		List<Task> tasks = new ArrayList<>();
// 		tasks.forEach(task -> {
// 			task.update();
// 		});
			
// 	}

// }

// class TaskForm{

// 	save(){

// 		List<Object> ids = new ArrayList();
// 		for(int i=0; i< ids.length; i++){
// 			if(ids[i] == 'Task.Status.Choices[1]'){
// 				System.out.println();
// 			}else if(ids[i] == 'Task.Status.Choices[2]'){
// 				System.out.println();
// 			} else{
// 				TaskBackground.executeTaskOLAUpdate();
// 			}
// 		}

// 	}


// }