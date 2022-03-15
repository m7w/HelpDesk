import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';
import SvgIcon from '@material-ui/core/SvgIcon';
import {ArrowDropDown} from '@material-ui/icons';


const styles = theme => ({
  root: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: theme.palette.background.paper,
  },
  colored: {
    backgroundColor: "blue",
  },
});

class DropDown extends React.Component {
  state = {
    anchorEl: null,
    selectedIndex: this.props.selectedIndex,
  };

  handleClickListItem = event => {
    this.setState({ anchorEl: event.currentTarget });
  };

  handleMenuItemClick = (index) => {
    this.setState({ selectedIndex: index, anchorEl: null });
    if (typeof this.props.onSelect === 'function') {
      console.log("In handleMenuItemClick");
      this.props.onSelect(index);
    }
  };

  handleClose = () => {
    this.setState({ anchorEl: null });
  };

  render() {
    const { handleClickListItem, handleMenuItemClick, handleClose } = this;
    const { label, options, currentIndex } = this.props;
    const { anchorEl, selectedIndex } = this.state;

    return (
      <div>
        <List component="nav">
          <ListItem
            style={{
              ...styles.colored,
            }}
            button
            aria-haspopup="true"
            aria-controls="lock-menu"
            aria-label={label}
            onClick={handleClickListItem}
          >
            <ListItemText
              primary={label}
              secondary={options[currentIndex ? currentIndex : selectedIndex].label}
            />
            <SvgIcon>
              <ArrowDropDown />
            </SvgIcon>
          </ListItem>
        </List>
        <Menu
          id="lock-menu"
          anchorEl={anchorEl}
          open={Boolean(anchorEl)}
          onClose={handleClose}
        >
          {options.map((option, index) => (
            <MenuItem
              key={option.label}
              disabled={index === selectedIndex}
              selected={index === selectedIndex}
              onClick={() => handleMenuItemClick(index)}
            >
              {option.label}
            </MenuItem>
          ))}
        </Menu>
      </div>
    );
  }
}

DropDown.propTypes = {
  options: PropTypes.array.isRequired,
};

export default withStyles(styles)(DropDown);
