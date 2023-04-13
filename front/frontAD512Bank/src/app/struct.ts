import { Person } from "./person";

export interface Struct {
    dn : string;
    title?: string;
    members? : Person[];
}