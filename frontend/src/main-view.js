import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import "@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout.js";
import "@vaadin/vaadin-ordered-layout/vaadin-vertical-layout.js";
import "@vaadin/vaadin-button/vaadin-button.js";
import "@polymer/iron-icon/iron-icon.js";
import "@vaadin/vaadin-icons/vaadin-icons.js";
import "@vaadin/vaadin-split-layout/vaadin-split-layout.js";
import "@vaadin/vaadin-grid/vaadin-grid.js";
import "./data-object-view.js";

class MainView extends PolymerElement {
    static get template() {
        return html`
        <style include="shared-styles">
            :host {
                display: block;
                height: 100%;
            }

            #header {
                background: #f3f5f8;
                width: 100%;
                padding-left: 25px;
            }
            #filesGrid,
            #projectTreeGrid {
                border: none;
            }
            .treeGridIcon {
                width: 18px;
                margin-right: 8px;
            }
        </style>

        <vaadin-vertical-layout style="width: 100%; height: 100%;">
            <vaadin-horizontal-layout id="header">
                <vaadin-button theme="tertiary" id="vaadinButton">
                    <iron-icon icon="vaadin:browser" slot="prefix"></iron-icon> Manage windows
                </vaadin-button>
                <vaadin-button theme="tertiary" id="vaadinButton1">
                    <iron-icon icon="vaadin:user" slot="prefix"></iron-icon> Technische Daten
                </vaadin-button>
                <vaadin-button theme="tertiary" id="vaadinButton2">
                    <iron-icon icon="vaadin:cog" slot="prefix"></iron-icon> Verwaltung
                </vaadin-button>
                <vaadin-button theme="tertiary" id="vaadinButton3">
                    SNMP Administration
                </vaadin-button>
                <vaadin-button theme="tertiary" id="vaadinButton4">
                    Just a button
                </vaadin-button>
            </vaadin-horizontal-layout>
            
            <vaadin-split-layout style="width: 100%; height: 100%;">
                <vaadin-split-layout orientation="vertical" style="width: 300px; height: 100%;">
                    <vaadin-grid id="projectTreeGrid" ></vaadin-grid>
                    <vaadin-grid id="filesGrid" rows-draggable></vaadin-grid>
                </vaadin-split-layout>
                <vaadin-horizontal-layout style="width: 100%; height: 100%;">
                    <data-object-view id="firstWindow"></data-object-view>
                    <data-object-view id="secondWindow"></data-object-view>
                </vaadin-horizontal-layout>
            </vaadin-split-layout>
        </vaadin-vertical-layout>
                `;
    }

    ready() {
        super.ready();
        let draggedItem;

        let filesGrid = this.$.filesGrid;

        this.$.firstWindow.addEventListener("drop", (e) => {
            if (draggedItem && draggedItem.projectId) {
                let itemDrop = new CustomEvent('item-dropped');
                itemDrop.id = draggedItem.projectId;
                this.$.firstWindow.dispatchEvent(itemDrop);
            }
            draggedItem = null;
        });

        this.$.secondWindow.addEventListener("drop", (e) => {
            if (draggedItem && draggedItem.projectId) {
                let itemDrop = new CustomEvent('item-dropped');
                itemDrop.id = draggedItem.projectId;
                this.$.secondWindow.dispatchEvent(itemDrop);
            }
            draggedItem = null;
        });

        filesGrid.addEventListener('grid-dragstart', function(e) {
            draggedItem = e.detail.draggedItems[0];
            filesGrid.dropMode = 'between';
        });

        filesGrid.addEventListener('grid-dragend', function(e) {
            filesGrid.dropMode = null;
        });
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }

    static get is() {
        return 'main-view';
    }
}
customElements.define(MainView.is, MainView);