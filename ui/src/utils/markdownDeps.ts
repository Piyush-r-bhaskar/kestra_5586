import markdownIt from "markdown-it";
import mark from "markdown-it-mark";
import meta from "markdown-it-meta";
import anchor from "markdown-it-anchor";
import container from "markdown-it-container";
import {fromHighlighter} from "@shikijs/markdown-it/core";
import {createHighlighterCore} from "shiki/core";
import githubDark from "shiki/themes/github-dark.mjs";
import githubLight from "shiki/themes/github-light.mjs";
import {linkTag} from "./markdown_plugins/link";

export {
    markdownIt,
    mark,
    meta,
    anchor,
    container,
    fromHighlighter,
    createHighlighterCore,
    githubDark,
    githubLight,
    linkTag
}