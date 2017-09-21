var MatchingProfile = React.createClass({
	render: function() {
		return(
			<tr>
				<td>{this.props.matchingprofile.display_name}</td>
				<td>{this.props.matchingprofile.age}</td>
				<td>{this.props.matchingprofile.job_title}</td>
				<td>{this.props.matchingprofile.height_in_cm}</td>
				<td>{this.props.matchingprofile.city.name}</td>
				<td><img src={this.props.matchingprofile.main_photo} alt="main_photo"></img></td>
				<td>{this.props.matchingprofile.compatibility_score}</td>
				<td>{this.props.matchingprofile.contacts_exchanged}</td>
				<td>{this.props.matchingprofile.favourite ? "True" : "False"}</td>
				<td>{this.props.matchingprofile.religion}</td>
			</tr>
		);
	}
});

var MatchingProfileTable = React.createClass({
	render: function() {
		var rows = [];
		this.props.matchingprofiles.forEach(function(mp) {
			rows.push(<MatchingProfile matchingprofile={mp} key={mp.id}></MatchingProfile>);
		});
		
		return(
			<table className="table table-striped">
				<thead>
					<tr>
						<th>Display Name</th>
						<th>Age</th>
						<th>Job Title</th>
						<th>Height(cm)</th>
						<th>City</th>
						<th>Main Photo</th>
						<th>Compatibility Score</th>
						<th>Contacts Exchanged</th>
						<th>Favourite</th>
						<th>Religion</th>
					</tr>
				</thead>
				<tbody>
					{rows}
				</tbody>
			</table>
		);
	}
});

var LoginUserDisp = React.createClass({
	loadLoginUser: function() {
//		console.log("LoginUserDisp.loadLoginUser called");
		var self = this;
		$.ajax({
			url: "http://localhost:8080/report/loggedin",
		}).then(function(data) {
			self.setState({loginuser: data});
		});
	},
	getInitialState: function() {
//		console.log("LoginUserDisp.getInitialState called");
		return {loginuser: {city:{}}};
	},
	componentDidMount: function() {
//		console.log("LoginUserDisp.componentDidMount called");
		this.loadLoginUser();
	},
	render: function() {
//		console.log("LoginUserDisp.render called");
		return(
			<div className="alert alert-info">
				Hello! You are currently online as <b>{this.state.loginuser.display_name}</b>
			</div>
		);
	}
});

var FilterMatches = React.createClass({
	getInitialState: function() {
//		console.log("FilterMatches.getInitialState called");
		return {
			filter: {
				hasPhoto: "",
				inContact: "",
				favourite: "",
				compScoreMin: "",
				compScoreMax: "",
				ageMin: "",
				ageMax: "",
				heightMin: "",
				heightMax: "",
				distanceMax: ""
			},
			distanceMaxOpt: "",
			matchingprofiles: []
		};
	},
	handleInputChange: function(event) {
//		console.log("FilterMatches.handleInputChange called");
		const target = event.target;
		const value = target.type === 'checkbox' ? target.checked : target.value;
		const name = target.name;
		const filters = this.state.filter;
		var changeVal = value;
		if(name === "distanceMaxOpt") {
			this.setState({distanceMaxOpt: changeVal, filters});
			return;
		}
		if((name === "ageMin" && value > 95)
			changeVal = 999;
		else if((name === "heightMin" && value > 210)
			changeVal = 999;
		filters[name] = changeVal;
		this.setState({
			filters,
		});
	},
	handleSubmit(event) {
		event.preventDefault();
//		console.log("FilterMatches.handleSubmit called");
		var clearOnRet = false;
		if(this.state.distanceMaxOpt === "0" || this.state.distanceMaxOpt === "-1") {
			this.state.filter.distanceMax = this.state.distanceMaxOpt;
			clearOnRet = true;
		}
		var self = this;
		$.ajax({
			url: "http://localhost:8080/report/matches",
			data: this.state.filter,
		}).then(function(data) {
//			console.log(data);
			if(clearOnRet)
				self.state.filter.distanceMax = "";
			self.setState({matchingprofiles: data});
		});
	},
	render: function() {
//		console.log("FilterMatches.render called");
		return (
			<div>
			<a className="btn btn-danger" href="./report">RESET FILTERS</a>
			<h1>Matching Profiles Report</h1>
			<form onSubmit={this.handleSubmit}>
				<label>
					Has Photo: 
					<input name="hasPhoto" type="checkbox" checked={this.state.filter.hasPhoto} onChange={this.handleInputChange}></input>
				</label>
				<br />
				<label>
					In Contact: 
					<input name="inContact" type="checkbox" checked={this.state.filter.inContact} onChange={this.handleInputChange}></input>
				</label>
				<br />
				<label>
					Favourite: 
					<input name="favourite" type="checkbox" checked={this.state.filter.favourite} onChange={this.handleInputChange}></input>
				</label>
				<br />
				<label>
					Compatibility Score Min: 
					<input name="compScoreMin" type="number" min="1" max="99" value={this.state.filter.compScoreMin} onChange={this.handleInputChange}></input>
				</label>
				<label>
					Max: 
					<input name="compScoreMax" type="number" min="1" max="99" value={this.state.filter.compScoreMax} onChange={this.handleInputChange}></input>
				</label>
				<br />
				<label>
					Age Min: 
					<input name="ageMin" type="number" min="18" max="999" value={this.state.filter.ageMin} onChange={this.handleInputChange}></input>
				</label>
				<label>
					Max: 
					<input name="ageMax" type="number" min="18" max="999" value={this.state.filter.ageMax} onChange={this.handleInputChange}></input>
				</label>
				<br />
				<label>
					Height(cm) Min: 
					<input name="heightMin" type="number" min="135" max="999" value={this.state.filter.heightMin} onChange={this.handleInputChange}></input>
				</label>
				<label>
					Max: 
					<input name="heightMax" type="number" min="135" max="999" value={this.state.filter.heightMax} onChange={this.handleInputChange}></input>
				</label>
				<br />
				<label>
					Distance(km): 
					<div className="radio">
						<label>
							<input type="radio" name="distanceMaxOpt" value="0" checked={this.state.distanceMaxOpt === '0'} onChange={this.handleInputChange}></input>
							Short Distance(&lt; 30 km)
						</label>
						<br />
						<label>
							<input type="radio" name="distanceMaxOpt" value="-1" checked={this.state.distanceMaxOpt === '-1'} onChange={this.handleInputChange}></input>
							Long Distance(&gt; 300 km)
						</label>
						<br />
						<label>
							<input type="radio" name="distanceMaxOpt" value="1" checked={this.state.distanceMaxOpt === '1'} onChange={this.handleInputChange}></input>
							Custom <input name="distanceMax" type="number" min="30" max="300" value={this.state.filter.distanceMax} onChange={this.handleInputChange}></input>
						</label>
					</div>
				</label>
				<br />
				<input className="btn btn-primary" type="submit" value="Filter" />
			</form>
			<hr />
			<MatchingProfileTable matchingprofiles={this.state.matchingprofiles}></MatchingProfileTable>
			</div>
		);
	}
});

ReactDOM.render(<LoginUserDisp />, document.getElementById('loginmatch'));
ReactDOM.render(<FilterMatches />, document.getElementById('filtermatches'));