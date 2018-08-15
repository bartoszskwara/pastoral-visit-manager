import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AddressDto} from "../../shared/model/AddressDto";

@Component({
  selector: 'drag-and-drop',
  templateUrl: './drag-and-drop.component.html',
  styleUrls: ['./drag-and-drop.component.scss']
})
export class DragAndDropComponent implements OnInit {

  @Output() uploaded = new EventEmitter<File>();

  constructor() { }

  ngOnInit() {
  }

  emit(file: File) {
    if(file != null) {
      this.uploaded.emit(file);
    }
  }

  onDrop(event: DragEvent) {
    event.preventDefault();

    if (event.dataTransfer.items && event.dataTransfer.items.length == 1) {
      if (event.dataTransfer.items[0].kind === 'file') {
        let file = event.dataTransfer.items[0].getAsFile();
        this.emit(file);
      }
    } else if(event.dataTransfer.files.length == 1) {
      let file = event.dataTransfer.files[0];
      this.emit(file);
    } else {
      this.emit(null);
    }

    this.removeDragData(event)
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    console.log('dragging over', event);
  }

  private removeDragData(event: DragEvent) {
    console.log('Removing drag data')
    if (event.dataTransfer.items) {
      event.dataTransfer.items.clear();
    } else {
      event.dataTransfer.clearData();
    }
  }
}
