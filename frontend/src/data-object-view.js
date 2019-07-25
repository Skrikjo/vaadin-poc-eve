import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';

import "@vaadin/vaadin-ordered-layout/vaadin-vertical-layout.js";
import "@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout.js";
import "@vaadin/vaadin-button/vaadin-button.js";
import "@polymer/iron-icon/iron-icon.js";
import "@vaadin/vaadin-icons/vaadin-icons.js";
import "@vaadin/vaadin-select/vaadin-select.js";
import "@vaadin/vaadin-text-field/vaadin-text-field.js";
import "@vaadin/vaadin-tabs/vaadin-tabs.js";
import "@vaadin/vaadin-tabs/vaadin-tab.js";

class DataObjectView extends PolymerElement {
    static get template() {
        return html`
          <style include="shared-styles">
            :host {
                width: 100%;
                height: 100%;
                border-right: 1px solid #f3f5f8;
            }

            :host[hidden="true"] {
                display:none !important;
            }

            #header {
                width: 100%;
                border-bottom: 1px solid #e9ebf0;
            }

            #content {
                padding: 10px 15px;

            }

            #title {
                padding: 5px 10px;
                font-weight: 700;
                width: 100%;
            }

            #icons-cont vaadin-button {
                margin-right: 5px;
            }

            #tabContent {
                height: calc(100% - 45px);
                overflow: auto;
            }

            #footer vaadin-button,
            #footer vaadin-text-field,
            #footer vaadin-select {
                margin-right: 5px;
            }

            #footerDate {
                padding-top: 8px;
                margin-right: 15px;
                margin-left: 5px;
            }

            #names {
                width: 100px;
            }

        </style>
        <!--<vaadin-vertical-layout style="width: 100%; height: 100%;" id="placeHolder">-->
            <!--<h3>No projects are selected. Please Drag & Drop Project here. </h3>-->
        <!--</vaadin-vertical-layout>-->
        <vaadin-vertical-layout style="width: 100%; height: 100%;" id="content">
            <vaadin-horizontal-layout id="header">
                <div id="title">
                    Title
                </div>
                <vaadin-button theme="icon tertiary" aria-label="Hide" id="hideBtn">
                    <iron-icon icon="vaadin:eye-slash"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon tertiary" aria-label="Full size" id="fullSizeBtn">
                    <iron-icon icon="vaadin:expand-square"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon tertiary" aria-label="Side by side" id="sideBySideBtn">
                    <iron-icon icon="vaadin:pause"></iron-icon>
                </vaadin-button>
            </vaadin-horizontal-layout>
            <vaadin-horizontal-layout style="width: 100%;">
                <vaadin-button theme="tertiary" id="details">
                    Details
                </vaadin-button>
                <vaadin-button theme="tertiary" id="diagram">
                    Diagrams
                </vaadin-button>
            </vaadin-horizontal-layout>
            <vaadin-horizontal-layout style="width: 100%;" id="icons-cont">
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:folder-open-o"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:folder-add"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:flag"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:plus-circle-o"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:angle-left"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:angle-right"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:search-plus"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:search-minus"></iron-icon>
                </vaadin-button>
            </vaadin-horizontal-layout>
            <div style="width: 100%; height: calc(100% - 175px);">
                <vaadin-tabs id="tabs"></vaadin-tabs>
                <div id="tabContent">
                    <h3 style="color: #979797">
                        Please Drag and Drop project from "Draggable Project" TreeGrid.
                    </h3>
                </div>
            </div>
            <vaadin-horizontal-layout style="width: 100%;" id="footer">
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:angle-left"></iron-icon>
                </vaadin-button>
                <vaadin-button theme="icon" aria-label="Add new">
                    <iron-icon icon="vaadin:angle-right"></iron-icon>
                </vaadin-button>
                <vaadin-select value="Jose" id="names">
                    <template>
                        <vaadin-list-box>
                            <vaadin-item>
                                Jose
                            </vaadin-item>
                            <vaadin-item>
                                Manolo
                            </vaadin-item>
                            <vaadin-item>
                                Pedro
                            </vaadin-item>
                        </vaadin-list-box>
                    </template>
                </vaadin-select>
                <span id="footerDate">Text</span>
                <vaadin-select value="Aplication layer 1">
                    <template>
                        <vaadin-list-box>
                            <vaadin-item>
                                Aplication layer 1
                            </vaadin-item>
                            <vaadin-item>
                                Aplication layer 2
                            </vaadin-item>
                            <vaadin-item>
                                Aplication layer 3
                            </vaadin-item>
                        </vaadin-list-box>
                    </template>
                </vaadin-select>
            </vaadin-horizontal-layout>
        </vaadin-vertical-layout>
                `;
    }

    ready() {
        super.ready();
        this.addEventListener("drop", this.drop.bind(this));
        this.addEventListener("dragleave", this.dragLeave.bind(this));
        this.addEventListener("dragover", this.dragOver.bind(this));

    }

    drop(e) {
        this.style.border = "";
        // this.style.padding = "10px 15px";
        this.$.content.style.padding = "10px 15px";
    }

    dragLeave(e) {
        // this.style.padding = "10px 15px";
        this.style.border = "";
        this.$.content.style.padding = "10px 15px";
    }

    dragOver(e) {
        e.dataTransfer.dropEffect = "copy";
        e.stopPropagation();
        e.preventDefault();
        this.style.border = "2px dashed red";
        this.$.content.style.padding = "8px 13px";
        // this.style.padding = "8px 13px";
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }

    static get is() {
        return 'data-object-view';
    }
}
customElements.define(DataObjectView.is, DataObjectView);